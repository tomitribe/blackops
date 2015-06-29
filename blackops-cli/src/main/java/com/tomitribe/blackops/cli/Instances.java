/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops.cli;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.Join;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command
public class Instances {

    public static void main(String[] args) throws Exception {
        final StreamingOutput list = new Instances().list(Arrays.asList(new Tag("none", null)));
        list.write(System.out);
    }

    @Command
    public StreamingOutput list() throws Exception {
        return list(Collections.EMPTY_LIST);
    }

    @Command
    public StreamingOutput list(final List<Tag> tag) throws Exception {
        final AmazonEC2Client client = new AmazonEC2Client(new BasicAWSCredentials("AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn"));
        final DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();

        describeInstancesRequest.withFilters(new Filter("instance-state-name").withValues("running"));

        for (final Tag t : tag) {
            if (t.getValue() == null) {
                describeInstancesRequest.withFilters(new Filter("tag-key").withValues(t.getKey()));
            } else {
                describeInstancesRequest.withFilters(new Filter("tag:" + t.getKey()).withValues(t.getValue()));
            }
        }


//        for (final KeyValue key : keys) {
//            final Filter filter = new Filter("tag:"+key.getKey() + "=" + key.getValue());
//            describeInstancesRequest.withFilters(filter);
//        }

        final DescribeInstancesResult result = client.describeInstances(describeInstancesRequest);

        return outputStream -> {
            final PrintStream out = new PrintStream(outputStream);

            result.getReservations().stream()
                    .flatMap(reservation -> reservation.getInstances().stream())
                    .sorted((o1, o2) -> o2.getLaunchTime().compareTo(o1.getLaunchTime()))
                    .collect(Collectors.toList())
                    .forEach(instance -> {
                        final String name = getName(instance);
                        out.printf("%-30s %-45s %-10s %-12s %-12s %-17s %-30s %s%n",
                                name,
                                instance.getPublicDnsName(),
                                instance.getState().getName(),
                                instance.getInstanceType(),
                                instance.getInstanceId(),
                                instance.getKeyName(),
                                instance.getLaunchTime(),
                                tags(instance)
                        );
                    });
            ;
        };
    }

    private String tags(final Instance instance) {
        final List<String> tags = instance.getTags().stream()
                .map(tag -> tag.getKey() + "=" + tag.getValue())
                .sorted(String::compareTo)
                .filter(s -> !s.startsWith("Name="))
                .filter(s -> !s.startsWith("aws:"))
                .filter(s -> !s.startsWith("elasticbeanstalk:"))
                .collect(Collectors.toList());

        return Join.join(", ", tags);
    }

    private String getName(final Instance instance) {
        for (final com.amazonaws.services.ec2.model.Tag tag : instance.getTags()) {
            if ("Name".equals(tag.getKey())) return tag.getValue();
        }
        return null;
    }


    public static class Tag {
        private final String key;
        private final String value;

        public Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public static Tag parse(final String raw) {
            final String[] split = raw.split(" *= *");

            if (split.length == 1) {
                return new Tag(split[0], null);
            }

            if (split.length == 2) {
                return new Tag(split[0], split[1]);
            }

            throw new IllegalArgumentException("Invalid tag format: " + raw);
        }
    }
}
