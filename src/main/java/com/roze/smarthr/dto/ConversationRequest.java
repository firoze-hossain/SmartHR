
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.ConversationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    private String title;

    @NotNull
    private ConversationType type;

    @NotNull
    private Set<Long> participantIds;
}