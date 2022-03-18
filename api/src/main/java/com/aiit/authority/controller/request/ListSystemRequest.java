package com.aiit.authority.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListSystemRequest extends ListBaseRequest implements Serializable {
}