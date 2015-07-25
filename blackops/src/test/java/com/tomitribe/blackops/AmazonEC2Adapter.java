/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;

public class AmazonEC2Adapter implements AmazonEC2 {

    @Override
    public void setEndpoint(String s) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRegion(Region region) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rebootInstances(RebootInstancesRequest rebootInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesResult describeReservedInstances(DescribeReservedInstancesRequest describeReservedInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateFlowLogsResult createFlowLogs(CreateFlowLogsRequest createFlowLogsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAvailabilityZonesResult describeAvailabilityZones(DescribeAvailabilityZonesRequest describeAvailabilityZonesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RestoreAddressToClassicResult restoreAddressToClassic(RestoreAddressToClassicRequest restoreAddressToClassicRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DetachVolumeResult detachVolume(DetachVolumeRequest detachVolumeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteKeyPair(DeleteKeyPairRequest deleteKeyPairRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnmonitorInstancesResult unmonitorInstances(UnmonitorInstancesRequest unmonitorInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttachVpnGatewayResult attachVpnGateway(AttachVpnGatewayRequest attachVpnGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateImageResult createImage(CreateImageRequest createImageRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSecurityGroup(DeleteSecurityGroupRequest deleteSecurityGroupRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateInstanceExportTaskResult createInstanceExportTask(CreateInstanceExportTaskRequest createInstanceExportTaskRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void authorizeSecurityGroupEgress(AuthorizeSecurityGroupEgressRequest authorizeSecurityGroupEgressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void associateDhcpOptions(AssociateDhcpOptionsRequest associateDhcpOptionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public GetPasswordDataResult getPasswordData(GetPasswordDataRequest getPasswordDataRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public StopInstancesResult stopInstances(StopInstancesRequest stopInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportKeyPairResult importKeyPair(ImportKeyPairRequest importKeyPairRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteNetworkInterface(DeleteNetworkInterfaceRequest deleteNetworkInterfaceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifyVpcAttribute(ModifyVpcAttributeRequest modifyVpcAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotFleetInstancesResult describeSpotFleetInstances(DescribeSpotFleetInstancesRequest describeSpotFleetInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateSecurityGroupResult createSecurityGroup(CreateSecurityGroupRequest createSecurityGroupRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotPriceHistoryResult describeSpotPriceHistory(DescribeSpotPriceHistoryRequest describeSpotPriceHistoryRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeNetworkInterfacesResult describeNetworkInterfaces(DescribeNetworkInterfacesRequest describeNetworkInterfacesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeRegionsResult describeRegions(DescribeRegionsRequest describeRegionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateDhcpOptionsResult createDhcpOptions(CreateDhcpOptionsRequest createDhcpOptionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateReservedInstancesListingResult createReservedInstancesListing(CreateReservedInstancesListingRequest createReservedInstancesListingRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteVpcEndpointsResult deleteVpcEndpoints(DeleteVpcEndpointsRequest deleteVpcEndpointsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetSnapshotAttribute(ResetSnapshotAttributeRequest resetSnapshotAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRoute(DeleteRouteRequest deleteRouteRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInternetGatewaysResult describeInternetGateways(DescribeInternetGatewaysRequest describeInternetGatewaysRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportVolumeResult importVolume(ImportVolumeRequest importVolumeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSecurityGroupsResult describeSecurityGroups(DescribeSecurityGroupsRequest describeSecurityGroupsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RejectVpcPeeringConnectionResult rejectVpcPeeringConnection(RejectVpcPeeringConnectionRequest rejectVpcPeeringConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteFlowLogsResult deleteFlowLogs(DeleteFlowLogsRequest deleteFlowLogsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detachVpnGateway(DetachVpnGatewayRequest detachVpnGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deregisterImage(DeregisterImageRequest deregisterImageRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotDatafeedSubscriptionResult describeSpotDatafeedSubscription(DescribeSpotDatafeedSubscriptionRequest describeSpotDatafeedSubscriptionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteTags(DeleteTagsRequest deleteTagsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSubnet(DeleteSubnetRequest deleteSubnetRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAccountAttributesResult describeAccountAttributes(DescribeAccountAttributesRequest describeAccountAttributesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttachClassicLinkVpcResult attachClassicLinkVpc(AttachClassicLinkVpcRequest attachClassicLinkVpcRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpnGatewayResult createVpnGateway(CreateVpnGatewayRequest createVpnGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableVolumeIO(EnableVolumeIORequest enableVolumeIORequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public MoveAddressToVpcResult moveAddressToVpc(MoveAddressToVpcRequest moveAddressToVpcRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVpnGateway(DeleteVpnGatewayRequest deleteVpnGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttachVolumeResult attachVolume(AttachVolumeRequest attachVolumeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVolumeStatusResult describeVolumeStatus(DescribeVolumeStatusRequest describeVolumeStatusRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImportSnapshotTasksResult describeImportSnapshotTasks(DescribeImportSnapshotTasksRequest describeImportSnapshotTasksRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpnConnectionsResult describeVpnConnections(DescribeVpnConnectionsRequest describeVpnConnectionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetImageAttribute(ResetImageAttributeRequest resetImageAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableVgwRoutePropagation(EnableVgwRoutePropagationRequest enableVgwRoutePropagationRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateSnapshotResult createSnapshot(CreateSnapshotRequest createSnapshotRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVolume(DeleteVolumeRequest deleteVolumeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateNetworkInterfaceResult createNetworkInterface(CreateNetworkInterfaceRequest createNetworkInterfaceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ModifyReservedInstancesResult modifyReservedInstances(ModifyReservedInstancesRequest modifyReservedInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelSpotFleetRequestsResult cancelSpotFleetRequests(CancelSpotFleetRequestsRequest cancelSpotFleetRequestsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unassignPrivateIpAddresses(UnassignPrivateIpAddressesRequest unassignPrivateIpAddressesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcsResult describeVpcs(DescribeVpcsRequest describeVpcsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelConversionTask(CancelConversionTaskRequest cancelConversionTaskRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AssociateAddressResult associateAddress(AssociateAddressRequest associateAddressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCustomerGateway(DeleteCustomerGatewayRequest deleteCustomerGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createNetworkAclEntry(CreateNetworkAclEntryRequest createNetworkAclEntryRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AcceptVpcPeeringConnectionResult acceptVpcPeeringConnection(AcceptVpcPeeringConnectionRequest acceptVpcPeeringConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeExportTasksResult describeExportTasks(DescribeExportTasksRequest describeExportTasksRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detachInternetGateway(DetachInternetGatewayRequest detachInternetGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpcPeeringConnectionResult createVpcPeeringConnection(CreateVpcPeeringConnectionRequest createVpcPeeringConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateRouteTableResult createRouteTable(CreateRouteTableRequest createRouteTableRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelImportTaskResult cancelImportTask(CancelImportTaskRequest cancelImportTaskRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVolumesResult describeVolumes(DescribeVolumesRequest describeVolumesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesListingsResult describeReservedInstancesListings(DescribeReservedInstancesListingsRequest describeReservedInstancesListingsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reportInstanceStatus(ReportInstanceStatusRequest reportInstanceStatusRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeRouteTablesResult describeRouteTables(DescribeRouteTablesRequest describeRouteTablesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeDhcpOptionsResult describeDhcpOptions(DescribeDhcpOptionsRequest describeDhcpOptionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public MonitorInstancesResult monitorInstances(MonitorInstancesRequest monitorInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribePrefixListsResult describePrefixLists(DescribePrefixListsRequest describePrefixListsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestSpotFleetResult requestSpotFleet(RequestSpotFleetRequest requestSpotFleetRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImportImageTasksResult describeImportImageTasks(DescribeImportImageTasksRequest describeImportImageTasksRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeNetworkAclsResult describeNetworkAcls(DescribeNetworkAclsRequest describeNetworkAclsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeBundleTasksResult describeBundleTasks(DescribeBundleTasksRequest describeBundleTasksRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportInstanceResult importInstance(ImportInstanceRequest importInstanceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void revokeSecurityGroupIngress(RevokeSecurityGroupIngressRequest revokeSecurityGroupIngressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteVpcPeeringConnectionResult deleteVpcPeeringConnection(DeleteVpcPeeringConnectionRequest deleteVpcPeeringConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public GetConsoleOutputResult getConsoleOutput(GetConsoleOutputRequest getConsoleOutputRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateInternetGatewayResult createInternetGateway(CreateInternetGatewayRequest createInternetGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVpnConnectionRoute(DeleteVpnConnectionRouteRequest deleteVpnConnectionRouteRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detachNetworkInterface(DetachNetworkInterfaceRequest detachNetworkInterfaceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifyImageAttribute(ModifyImageAttributeRequest modifyImageAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateCustomerGatewayResult createCustomerGateway(CreateCustomerGatewayRequest createCustomerGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateSpotDatafeedSubscriptionResult createSpotDatafeedSubscription(CreateSpotDatafeedSubscriptionRequest createSpotDatafeedSubscriptionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attachInternetGateway(AttachInternetGatewayRequest attachInternetGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVpnConnection(DeleteVpnConnectionRequest deleteVpnConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeMovingAddressesResult describeMovingAddresses(DescribeMovingAddressesRequest describeMovingAddressesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeConversionTasksResult describeConversionTasks(DescribeConversionTasksRequest describeConversionTasksRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpnConnectionResult createVpnConnection(CreateVpnConnectionRequest createVpnConnectionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportImageResult importImage(ImportImageRequest importImageRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DisableVpcClassicLinkResult disableVpcClassicLink(DisableVpcClassicLinkRequest disableVpcClassicLinkRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInstanceAttributeResult describeInstanceAttribute(DescribeInstanceAttributeRequest describeInstanceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeFlowLogsResult describeFlowLogs(DescribeFlowLogsRequest describeFlowLogsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcPeeringConnectionsResult describeVpcPeeringConnections(DescribeVpcPeeringConnectionsRequest describeVpcPeeringConnectionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribePlacementGroupsResult describePlacementGroups(DescribePlacementGroupsRequest describePlacementGroupsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RunInstancesResult runInstances(RunInstancesRequest runInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSubnetsResult describeSubnets(DescribeSubnetsRequest describeSubnetsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AssociateRouteTableResult associateRouteTable(AssociateRouteTableRequest associateRouteTableRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInstancesResult describeInstances(DescribeInstancesRequest describeInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifyVolumeAttribute(ModifyVolumeAttributeRequest modifyVolumeAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteNetworkAcl(DeleteNetworkAclRequest deleteNetworkAclRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImagesResult describeImages(DescribeImagesRequest describeImagesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public StartInstancesResult startInstances(StartInstancesRequest startInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifyInstanceAttribute(ModifyInstanceAttributeRequest modifyInstanceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelReservedInstancesListingResult cancelReservedInstancesListing(CancelReservedInstancesListingRequest cancelReservedInstancesListingRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteDhcpOptions(DeleteDhcpOptionsRequest deleteDhcpOptionsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotInstanceRequestsResult describeSpotInstanceRequests(DescribeSpotInstanceRequestsRequest describeSpotInstanceRequestsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpcResult createVpc(CreateVpcRequest createVpcRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeCustomerGatewaysResult describeCustomerGateways(DescribeCustomerGatewaysRequest describeCustomerGatewaysRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelExportTask(CancelExportTaskRequest cancelExportTaskRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateRouteResult createRoute(CreateRouteRequest createRouteRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpcEndpointResult createVpcEndpoint(CreateVpcEndpointRequest createVpcEndpointRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CopyImageResult copyImage(CopyImageRequest copyImageRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcClassicLinkResult describeVpcClassicLink(DescribeVpcClassicLinkRequest describeVpcClassicLinkRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifyNetworkInterfaceAttribute(ModifyNetworkInterfaceAttributeRequest modifyNetworkInterfaceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRouteTable(DeleteRouteTableRequest deleteRouteTableRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeNetworkInterfaceAttributeResult describeNetworkInterfaceAttribute(DescribeNetworkInterfaceAttributeRequest describeNetworkInterfaceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeClassicLinkInstancesResult describeClassicLinkInstances(DescribeClassicLinkInstancesRequest describeClassicLinkInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestSpotInstancesResult requestSpotInstances(RequestSpotInstancesRequest requestSpotInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createTags(CreateTagsRequest createTagsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVolumeAttributeResult describeVolumeAttribute(DescribeVolumeAttributeRequest describeVolumeAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttachNetworkInterfaceResult attachNetworkInterface(AttachNetworkInterfaceRequest attachNetworkInterfaceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceRoute(ReplaceRouteRequest replaceRouteRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeTagsResult describeTags(DescribeTagsRequest describeTagsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelBundleTaskResult cancelBundleTask(CancelBundleTaskRequest cancelBundleTaskRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disableVgwRoutePropagation(DisableVgwRoutePropagationRequest disableVgwRoutePropagationRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportSnapshotResult importSnapshot(ImportSnapshotRequest importSnapshotRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelSpotInstanceRequestsResult cancelSpotInstanceRequests(CancelSpotInstanceRequestsRequest cancelSpotInstanceRequestsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotFleetRequestsResult describeSpotFleetRequests(DescribeSpotFleetRequestsRequest describeSpotFleetRequestsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PurchaseReservedInstancesOfferingResult purchaseReservedInstancesOffering(PurchaseReservedInstancesOfferingRequest purchaseReservedInstancesOfferingRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifySnapshotAttribute(ModifySnapshotAttributeRequest modifySnapshotAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesModificationsResult describeReservedInstancesModifications(DescribeReservedInstancesModificationsRequest describeReservedInstancesModificationsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public TerminateInstancesResult terminateInstances(TerminateInstancesRequest terminateInstancesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ModifyVpcEndpointResult modifyVpcEndpoint(ModifyVpcEndpointRequest modifyVpcEndpointRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSpotDatafeedSubscription(DeleteSpotDatafeedSubscriptionRequest deleteSpotDatafeedSubscriptionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInternetGateway(DeleteInternetGatewayRequest deleteInternetGatewayRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSnapshotAttributeResult describeSnapshotAttribute(DescribeSnapshotAttributeRequest describeSnapshotAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReplaceRouteTableAssociationResult replaceRouteTableAssociation(ReplaceRouteTableAssociationRequest replaceRouteTableAssociationRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAddressesResult describeAddresses(DescribeAddressesRequest describeAddressesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImageAttributeResult describeImageAttribute(DescribeImageAttributeRequest describeImageAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeKeyPairsResult describeKeyPairs(DescribeKeyPairsRequest describeKeyPairsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConfirmProductInstanceResult confirmProductInstance(ConfirmProductInstanceRequest confirmProductInstanceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disassociateRouteTable(DisassociateRouteTableRequest disassociateRouteTableRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcAttributeResult describeVpcAttribute(DescribeVpcAttributeRequest describeVpcAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void revokeSecurityGroupEgress(RevokeSecurityGroupEgressRequest revokeSecurityGroupEgressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteNetworkAclEntry(DeleteNetworkAclEntryRequest deleteNetworkAclEntryRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVolumeResult createVolume(CreateVolumeRequest createVolumeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInstanceStatusResult describeInstanceStatus(DescribeInstanceStatusRequest describeInstanceStatusRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpnGatewaysResult describeVpnGateways(DescribeVpnGatewaysRequest describeVpnGatewaysRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateSubnetResult createSubnet(CreateSubnetRequest createSubnetRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesOfferingsResult describeReservedInstancesOfferings(DescribeReservedInstancesOfferingsRequest describeReservedInstancesOfferingsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void assignPrivateIpAddresses(AssignPrivateIpAddressesRequest assignPrivateIpAddressesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotFleetRequestHistoryResult describeSpotFleetRequestHistory(DescribeSpotFleetRequestHistoryRequest describeSpotFleetRequestHistoryRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSnapshot(DeleteSnapshotRequest deleteSnapshotRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReplaceNetworkAclAssociationResult replaceNetworkAclAssociation(ReplaceNetworkAclAssociationRequest replaceNetworkAclAssociationRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disassociateAddress(DisassociateAddressRequest disassociateAddressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createPlacementGroup(CreatePlacementGroupRequest createPlacementGroupRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BundleInstanceResult bundleInstance(BundleInstanceRequest bundleInstanceRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePlacementGroup(DeletePlacementGroupRequest deletePlacementGroupRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void modifySubnetAttribute(ModifySubnetAttributeRequest modifySubnetAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteVpc(DeleteVpcRequest deleteVpcRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CopySnapshotResult copySnapshot(CopySnapshotRequest copySnapshotRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcEndpointServicesResult describeVpcEndpointServices(DescribeVpcEndpointServicesRequest describeVpcEndpointServicesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AllocateAddressResult allocateAddress(AllocateAddressRequest allocateAddressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void releaseAddress(ReleaseAddressRequest releaseAddressRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetInstanceAttribute(ResetInstanceAttributeRequest resetInstanceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateKeyPairResult createKeyPair(CreateKeyPairRequest createKeyPairRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceNetworkAclEntry(ReplaceNetworkAclEntryRequest replaceNetworkAclEntryRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSnapshotsResult describeSnapshots(DescribeSnapshotsRequest describeSnapshotsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateNetworkAclResult createNetworkAcl(CreateNetworkAclRequest createNetworkAclRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisterImageResult registerImage(RegisterImageRequest registerImageRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetNetworkInterfaceAttribute(ResetNetworkInterfaceAttributeRequest resetNetworkInterfaceAttributeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnableVpcClassicLinkResult enableVpcClassicLink(EnableVpcClassicLinkRequest enableVpcClassicLinkRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createVpnConnectionRoute(CreateVpnConnectionRouteRequest createVpnConnectionRouteRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcEndpointsResult describeVpcEndpoints(DescribeVpcEndpointsRequest describeVpcEndpointsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DetachClassicLinkVpcResult detachClassicLinkVpc(DetachClassicLinkVpcRequest detachClassicLinkVpcRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesResult describeReservedInstances() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAvailabilityZonesResult describeAvailabilityZones() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotPriceHistoryResult describeSpotPriceHistory() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeNetworkInterfacesResult describeNetworkInterfaces() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeRegionsResult describeRegions() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInternetGatewaysResult describeInternetGateways() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSecurityGroupsResult describeSecurityGroups() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotDatafeedSubscriptionResult describeSpotDatafeedSubscription() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAccountAttributesResult describeAccountAttributes() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVolumeStatusResult describeVolumeStatus() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImportSnapshotTasksResult describeImportSnapshotTasks() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpnConnectionsResult describeVpnConnections() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcsResult describeVpcs() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AcceptVpcPeeringConnectionResult acceptVpcPeeringConnection() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeExportTasksResult describeExportTasks() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateVpcPeeringConnectionResult createVpcPeeringConnection() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CancelImportTaskResult cancelImportTask() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVolumesResult describeVolumes() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesListingsResult describeReservedInstancesListings() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeRouteTablesResult describeRouteTables() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeDhcpOptionsResult describeDhcpOptions() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribePrefixListsResult describePrefixLists() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImportImageTasksResult describeImportImageTasks() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeNetworkAclsResult describeNetworkAcls() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeBundleTasksResult describeBundleTasks() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void revokeSecurityGroupIngress() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreateInternetGatewayResult createInternetGateway() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeMovingAddressesResult describeMovingAddresses() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeConversionTasksResult describeConversionTasks() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportImageResult importImage() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeFlowLogsResult describeFlowLogs() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcPeeringConnectionsResult describeVpcPeeringConnections() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribePlacementGroupsResult describePlacementGroups() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSubnetsResult describeSubnets() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInstancesResult describeInstances() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeImagesResult describeImages() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotInstanceRequestsResult describeSpotInstanceRequests() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeCustomerGatewaysResult describeCustomerGateways() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcClassicLinkResult describeVpcClassicLink() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeClassicLinkInstancesResult describeClassicLinkInstances() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeTagsResult describeTags() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImportSnapshotResult importSnapshot() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSpotFleetRequestsResult describeSpotFleetRequests() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesModificationsResult describeReservedInstancesModifications() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSpotDatafeedSubscription() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeAddressesResult describeAddresses() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeKeyPairsResult describeKeyPairs() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeInstanceStatusResult describeInstanceStatus() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpnGatewaysResult describeVpnGateways() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeReservedInstancesOfferingsResult describeReservedInstancesOfferings() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcEndpointServicesResult describeVpcEndpointServices() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AllocateAddressResult allocateAddress() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeSnapshotsResult describeSnapshots() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DescribeVpcEndpointsResult describeVpcEndpoints() throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X extends AmazonWebServiceRequest> DryRunResult<X> dryRun(DryRunSupportedRequest<X> dryRunSupportedRequest) throws AmazonClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest amazonWebServiceRequest) {
        throw new UnsupportedOperationException();
    }
}
