// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: router_grpc.proto

package com.vearch.grpc.entity;

public interface MSearchRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:MSearchRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.RequestHead head = 1;</code>
   * @return Whether the head field is set.
   */
  boolean hasHead();
  /**
   * <code>.RequestHead head = 1;</code>
   * @return The head.
   */
  RequestHead getHead();
  /**
   * <code>.RequestHead head = 1;</code>
   */
  RequestHeadOrBuilder getHeadOrBuilder();

  /**
   * <code>repeated .SearchRequest search_requests = 2;</code>
   */
  java.util.List<SearchRequest>
      getSearchRequestsList();
  /**
   * <code>repeated .SearchRequest search_requests = 2;</code>
   */
  SearchRequest getSearchRequests(int index);
  /**
   * <code>repeated .SearchRequest search_requests = 2;</code>
   */
  int getSearchRequestsCount();
  /**
   * <code>repeated .SearchRequest search_requests = 2;</code>
   */
  java.util.List<? extends SearchRequestOrBuilder>
      getSearchRequestsOrBuilderList();
  /**
   * <code>repeated .SearchRequest search_requests = 2;</code>
   */
  SearchRequestOrBuilder getSearchRequestsOrBuilder(
          int index);
}