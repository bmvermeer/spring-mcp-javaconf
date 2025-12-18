package nl.brianvermeer.springmcpjavaconf;

import nl.brianvermeer.springmcpjavaconf.javaconfereces.Event;
import nl.brianvermeer.springmcpjavaconf.javaconfereces.JavaConferencesService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConferenceService {

    @Autowired
    JavaConferencesService javaConferencesService;

    @Tool(name = "javaConf_get_allEvents", description = "Retrieve all Java conferences, both past and upcoming, from the Java Conferences API at javaconferences.org.")
    public List<Event> getAllEvents() throws IOException {
        return javaConferencesService.getAllEvents();
    }

    @Tool(name = "javaConf_get_upcommingEvents", description = "Fetch all upcoming Java conferences from javaconferences.org whose start date is after the current date.")
    public List<Event> getUpcomingEvents() throws IOException {
        return javaConferencesService.getAllEvents().stream()
                .filter(event -> event.startDate().isAfter(java.time.LocalDateTime.now()))
                .toList();
    }

    @Tool(name = "javaConf_get_openCfps", description = "List all Java conferences from javaconferences.org with an open Call for Papers (CFP), where the CFP end date is after today.")
    public List<Event> getOpenCfps() throws IOException {
        return javaConferencesService.getAllEvents().stream()
                .filter(event -> event.CfpEndDate() != null && event.CfpEndDate().isAfter(java.time.LocalDate.now()))
                .toList();
    }

    @Tool(name = "javaConf_get_eventsInTimeframe", description = "Retrieve all Java conferences from javaconferences.org whose start date falls within the specified start and end date (inclusive). Provide start and end as ISO-8601 LocalDateTime.")
    public List<Event> getEventsInTimeframe(java.time.LocalDateTime start, java.time.LocalDateTime end) throws IOException {
        return javaConferencesService.getAllEvents().stream()
                .filter(event -> {
                    java.time.LocalDateTime eventStart = event.startDate();
                    return (eventStart.isEqual(start) || eventStart.isAfter(start)) &&
                           (eventStart.isEqual(end) || eventStart.isBefore(end));
                })
                .toList();
    }
}
