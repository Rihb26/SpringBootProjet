package com.example.SpringProjet;

import com.example.SpringProjet.Event.MessageUpdatedEvent;
import com.example.SpringProjet.Repository.*;
import com.example.SpringProjet.Service.ConversationService;
import com.example.SpringProjet.Service.FriendRequestService;
import com.example.SpringProjet.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SpringProjetApplicationTests {

	private List<Conversation> mockConversations;
	private List<FriendRequest> mockFriendRequests;

	private ConversationService conversationService;
	private FriendRequestService friendRequestService;

	@BeforeEach
	public void setUp() {
		// Simuler les données

		mockConversations = new ArrayList<>();
		Conversation conversation1 = new Conversation();
		conversation1.setId(1L);
		conversation1.setParticipant("John Doe");
		conversation1.setLastMessage("Hello!");

		Conversation conversation2 = new Conversation();
		conversation2.setId(2L);
		conversation2.setParticipant("Jane Smith");
		conversation2.setLastMessage("How are you?");

		mockConversations.add(conversation1);
		mockConversations.add(conversation2);

		mockFriendRequests = new ArrayList<>();
		FriendRequest request1 = new FriendRequest();
		request1.setId(1L);
		request1.setSenderId(1L);
		request1.setReceiverId(2L);
		request1.setStatus("PENDING");

		FriendRequest request2 = new FriendRequest();
		request2.setId(2L);
		request2.setSenderId(3L);
		request2.setReceiverId(4L);
		request2.setStatus("ACCEPTED");

		mockFriendRequests.add(request1);
		mockFriendRequests.add(request2);

		// Créer les mocks pour les services
		conversationService = mock(ConversationService.class);
		friendRequestService = mock(FriendRequestService.class);

		// Configurer les comportements simulés
		when(conversationService.getAllConversations()).thenReturn(mockConversations);
		when(friendRequestService.getReceivedRequests(anyLong())).thenAnswer(invocation -> {
			Long receiverId = invocation.getArgument(0);
			return mockFriendRequests.stream()
					.filter(req -> req.getReceiverId().equals(receiverId))
					.collect(Collectors.toList());
		});
		when(friendRequestService.sendFriendRequest(anyLong(), anyLong())).thenAnswer(invocation -> {
			Long senderId = invocation.getArgument(0);
			Long receiverId = invocation.getArgument(1);
			FriendRequest newRequest = new FriendRequest();
			newRequest.setId((long) (mockFriendRequests.size() + 1));
			newRequest.setSenderId(senderId);
			newRequest.setReceiverId(receiverId);
			newRequest.setStatus("PENDING");
			mockFriendRequests.add(newRequest);
			return newRequest;
		});
	}

	@Test
	public void testListAllConversations_Story1() { // story 1
		// Tester la méthode
		List<Conversation> result = conversationService.getAllConversations();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("John Doe", result.get(0).getParticipant());
		assertEquals("Jane Smith", result.get(1).getParticipant());
	}

	@Test
	public void testSendFriendRequest_Story2() { //story 2
		// Envoyer une demande d'ami
		FriendRequest newRequest = friendRequestService.sendFriendRequest(5L, 6L);

		assertNotNull(newRequest);
		assertEquals(3L, newRequest.getId());
		assertEquals("PENDING", newRequest.getStatus());
		assertEquals(3, mockFriendRequests.size());
	}

	@Test
	public void testListReceivedFriendRequests_Story3() { // story 3
		// Lister les demandes reçues pour le receiverId 2
		List<FriendRequest> receivedRequests = friendRequestService.getReceivedRequests(2L);

		assertNotNull(receivedRequests);
		assertEquals(1, receivedRequests.size());
		assertEquals(1L, receivedRequests.get(0).getId());
	}
	@Test
	public void testSendMessageToFriend_Story6() {
		// Configurer les comportements simulés
		when(conversationService.sendMessageToConversation(eq(1L), eq("How are you?"))).thenAnswer(invocation -> {
			Long conversationId = invocation.getArgument(0);
			String message = invocation.getArgument(1);

			// Trouver la conversation
			for (Conversation conversation : mockConversations) {
				if (conversation.getId().equals(conversationId)) {
					conversation.setLastMessage(message);
					conversation.setLastMessageTime(LocalDateTime.now());
					return true; // Retourne vrai si le message a été envoyé avec succès
				}
			}
			return false; // Retourne faux si la conversation n'existe pas
		});

		// Tester l'envoi d'un message
		boolean isMessageSent = conversationService.sendMessageToConversation(1L, "How are you?");

		// Vérifier que le message a bien été envoyé
		assertTrue(isMessageSent);

		// Vérifier que le message est mis à jour dans la conversation
		Conversation updatedConversation = mockConversations.get(0);
		assertEquals("How are you?", updatedConversation.getLastMessage());
		assertNotNull(updatedConversation.getLastMessageTime());
	}

    @Nested
	public class MessageServiceTest {

		@Mock
		private MessageRepository messageRepository;

		@Mock
		private ConversationRepository conversationRepository;

		@Mock
		private ApplicationEventPublisher eventPublisher;

		@InjectMocks
		private MessageService messageService;

		private Message message;

		@BeforeEach
		public void setUp() {
			// Initialiser les mocks avant chaque test
			MockitoAnnotations.openMocks(this);

			// Créer un message pour les tests
			message = new Message();
			message.setId(1L);
			message.setContent("Ancien message");
		}

		@Test
		public void testUpdateMessage_NotFound_Story9() {
			Long messageId = 999L; // ID qui n'existe pas

			// Configuration des mocks pour le cas où le message n'est pas trouvé
			when(messageRepository.findById(messageId)).thenReturn(Optional.empty()); // Retourne Optional.empty pour simuler un message non trouvé

			// Appel de la méthode à tester et vérification qu'une exception est lancée
			assertThrows(RuntimeException.class, () -> messageService.updateMessage(messageId, "Nouveau contenu"));
		}
	}

	@Nested
	public class FriendRequestServiceTest {

		@Test
		public void testDeclineFriendRequest() {
			Long requestId = 1L; // Demande d'ami à décliner

			// Configurer le comportement pour simuler la liste de demandes reçues
			when(friendRequestService.getReceivedRequests(2L)).thenReturn(mockFriendRequests);

			// Appeler la méthode declineFriendRequest
			friendRequestService.declineFriendRequest(requestId);

			// Vérifier que la taille de la liste de demandes d'ami a bien diminué
			assertEquals(1, mockFriendRequests.size()); // Il devrait rester une demande après la suppression

			// Vérifier que la demande avec l'ID 1 a bien été supprimée
			assertNull(mockFriendRequests.stream()
					.filter(req -> req.getId().equals(requestId))
					.findFirst()
					.orElse(null)); // La demande d'ami avec l'ID 1 devrait être supprimée
		}


		@Test
		public void testDeclineFriendRequest_NotFound() {
			Long requestId = 999L; // ID qui n'existe pas

			// Configuration du mock pour retourner une liste de demandes reçues
			when(friendRequestService.getReceivedRequests(2L)).thenReturn(mockFriendRequests);

			// Appeler la méthode declineFriendRequest avec un ID qui n'existe pas
			friendRequestService.declineFriendRequest(requestId);

			// Vérifier que la taille de la liste de demandes d'amis ne change pas (demande inexistante)
			assertEquals(2, mockFriendRequests.size());
		}
	}


}


