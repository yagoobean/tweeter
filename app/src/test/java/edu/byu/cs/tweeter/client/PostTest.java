package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class PostTest {

    MainActivity mockView;
    StatusService mockService;
    MainActivityPresenter spyPresenter;
    String testString;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainActivity.class);

        mockService = Mockito.mock(StatusService.class);
        spyPresenter = Mockito.spy(new MainActivityPresenter(mockView));

        // todo
        Mockito.when(spyPresenter.getStatusService()).thenReturn(mockService);

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // check if it triggers success or failure
                String status = invocation.getArgument(0);
                MainActivityPresenter.StatusObserver observer = invocation.getArgument(1);

                Assertions.assertNotNull(status);
                Assertions.assertNotNull(observer);
                // check the class or something idk

                if (testString.equals("Successfully Posted!")) {
                    observer.handleSuccess();

                } else if (testString.equals("Failed to post status:")) {
                    observer.displayError(testString);  // I feel like this is wrong

                } else if (testString.equals("Failed to post the status because of exception:")) {
                    observer.displayException(new Exception()); // fixme?

                }

                return null;
            }
        }).when(mockService).executeStatusTask(Mockito.any(), Mockito.any());
    }

    // TODO: Test that the Presenter's "post status" operation works correctly in all
    //  three of the outcomes listed above (succeeded, failed, failed due to exception).
    //      If succeeded, message should be "Posting Status..."
    //      If failed, message should start with "Failed to post status:"
    //      If failed from exception, message should start with "Failed to post the status
    //       because of exception: "
    @Test
    public void testSuccess() {
        testString = "Successfully Posted!";

        spyPresenter.executeStatusTask("Hello please work pls");
        Mockito.verify(mockView).postStatus();
        // verify that it posted
        // call postStatus on presenter
        // verify that it was called

    }

    // test for error
    @Test
    public void testFail() {
        testString = "Failed to post status:";
        // make sure view called w right message
        // Mockito.verify(displayMessage())
        spyPresenter.executeStatusTask("lkafjsdlkfa;sdlf");
        Mockito.verify(mockView).displayMessage(testString);
    }

    // test for exception
    @Test
    public void testException() {
        testString = "Failed to post the status because of exception:";
        spyPresenter.executeStatusTask("they're holding me hostage and making me film these commercials");
        Mockito.verify(mockView).displayMessage(testString);
    }

}
