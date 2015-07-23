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

import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.tomitribe.blackops.Aws;
import com.tomitribe.blackops.Tag;
import org.tomitribe.crest.api.Command;
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

        final DescribeInstancesRequest describeInstancesRequest = Aws.describeRunningInstances(tag);

        final DescribeInstancesResult result = Aws.client().describeInstances(describeInstancesRequest);

        final List<Instance> instances = Aws.getInstances(result);

        return listInstances(instances);
    }

    public static StreamingOutput listInstances(List<Instance> instances) {
        return outputStream -> {
            final PrintStream out = new PrintStream(outputStream);
            instances.forEach(instance -> print(out, instance));
        };
    }

    public static void print(PrintStream out, Instance instance) {
        final String name = Aws.getName(instance);
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
    }

    private static String tags(final Instance instance) {
        final List<String> tags = instance.getTags().stream()
                .map(tag -> tag.getKey() + "=" + tag.getValue())
                .sorted(String::compareTo)
                .filter(s -> !s.startsWith("Name="))
                .filter(s -> !s.startsWith("aws:"))
                .filter(s -> !s.startsWith("elasticbeanstalk:"))
                .collect(Collectors.toList());

        return Join.join(", ", tags);
    }

}
