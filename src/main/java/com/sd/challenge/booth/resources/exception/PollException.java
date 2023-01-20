package com.sd.challenge.booth.resources.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class PollException extends RuntimeException {

    String message;
    Map<String, String> data;

}
