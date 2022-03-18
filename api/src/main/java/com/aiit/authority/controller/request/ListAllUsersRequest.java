package com.aiit.authority.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListAllUsersRequest extends ListBaseRequest implements Serializable {

}
