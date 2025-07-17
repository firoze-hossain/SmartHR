
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private Long conversationId;
    private EmployeeResponse sender;
    private String content;
    private LocalDateTime sentAt;
    private MessageStatus status;

    public String getSenderName() {
        return sender != null ? sender.getName() : "Unknown";
    }
}