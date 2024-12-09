import com.example.SpringProjet.Dto.MessageSnapshot;
import com.example.SpringProjet.Repository.*;
import com.example.SpringProjet.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendRequestServiceTest {


    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage_Success() {
        // Arrange
        Long conversationId = 1L;
        Long senderId = 2L;
        String content = "Hello!";

        // Création de la conversation avec un nom
        Conversation conversation = new Conversation("Test Conversation");
        conversation.setId(conversationId); // Assigner un ID à la conversation

        // Création d'un message avec le contenu et la conversation
        Message message = new Message(conversation, senderId, content);

        // Création du snapshot du message
        MessageSnapshot expectedSnapshot = message.toSnapshot();

        // Définir le comportement du mock pour la conversation et le message
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        MessageSnapshot result = messageService.sendMessage(conversationId, senderId, content);

        // Assert
        assertNotNull(result);
        // Comparer les valeurs du snapshot
        assertEquals(expectedSnapshot.getConversationId(), result.getConversationId());
        assertEquals(expectedSnapshot.getSenderId(), result.getSenderId());
        assertEquals(expectedSnapshot.getContent(), result.getContent());
        assertEquals(expectedSnapshot.getCreatedAt(), result.getCreatedAt());

        // Vérification des appels aux mocks
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendMessage_ConversationNotFound() {
        // Arrange
        Long conversationId = 1L;
        Long senderId = 2L;
        String content = "Hello!";

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(conversationId, senderId, content);
        });

        assertEquals("Conversation inexistante.", exception.getMessage());
        verify(conversationRepository, times(1)).findById(conversationId);
        verifyNoInteractions(messageRepository);
    }
}