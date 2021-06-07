import JiraSubSystem.JiraHandler;
import JiraSubSystem.Resolution;

import java.io.IOException;
import java.util.List;

class JiraHandlerTest {

    @org.junit.jupiter.api.Test
    void getID() throws IOException {
        JiraHandler jiraHandler = new JiraHandler("S2GRAPH");
        List<String> IDList = jiraHandler.getBugTicketID(Resolution.done);
        for (String ID : IDList) {
            System.out.println(ID);
        }
    }

}