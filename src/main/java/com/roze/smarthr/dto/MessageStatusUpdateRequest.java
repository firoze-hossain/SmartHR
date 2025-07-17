
package com.roze.smarthr.dto;

import com.roze.smarthr.enums.MessageStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageStatusUpdateRequest {
    @NotNull
    private MessageStatus status;
}