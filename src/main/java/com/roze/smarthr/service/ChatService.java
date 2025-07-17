
package com.roze.smarthr.service;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.MessageStatus;

import java.util.List;

public interface ChatService {
    ConversationResponse createConversation(ConversationRequest request, User user);

    List<ConversationResponse> getUserConversations(User user);

    ConversationResponse getConversation(Long conversationId, User user);

    MessageResponse sendMessage(MessageRequest request, User user);

    List<MessageResponse> getConversationMessages(Long conversationId, User user);

    void updateMessageStatus(Long messageId, MessageStatusUpdateRequest request, User user);

    void notifyNewMessage(MessageResponse message);

    void notifyMessageStatusUpdate(Long messageId, MessageStatus status);
}