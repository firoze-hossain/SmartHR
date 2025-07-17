
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.ConversationType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long id;
    private String title;
    private ConversationType type;
    private Set<EmployeeResponse> participants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long unreadCount;
}