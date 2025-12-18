package nl.brianvermeer.springmcpjavaconf.javaconfereces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static nl.brianvermeer.springmcpjavaconf.javaconfereces.DateParser.parseDateRange;


@Service
public class JavaConferencesService {

    private static final String FEED_URL = "https://javaconferences.org/conferences.json";

    ObjectMapper mapper = new ObjectMapper();

    public List<Event> getAllEvents() throws IOException {
        List<ConferenceJson> conferences = fetchJavaConferences();
        return convertToEvents(conferences);
    }

    private List<ConferenceJson> fetchJavaConferences() throws IOException {
        mapper.registerModule(new JavaTimeModule());
        URL url = new URL(FEED_URL);
        return mapper.readValue(url, new TypeReference<List<ConferenceJson>>() {});
    }

    private List<Event> convertToEvents(List<ConferenceJson> conferences) {
        return conferences.stream()
                .map(this::convertToEvent)
                .toList();
    }

    private Event convertToEvent(ConferenceJson conference) {
        LocalDateTime[] dateRange = parseDateRange(conference.date());

        LocalDate cfpEndDate = null;
        if (conference.cfpEndDate() != null && !conference.cfpEndDate().isEmpty()) {
            cfpEndDate = parseDateRange(conference.cfpEndDate())[1].toLocalDate();
        }

        return new Event(
                conference.name(),
                dateRange[0],
                dateRange[1],
                conference.link(),
                conference.locationName(),
                determineFormat(conference),
                conference.cfpLink(),
                cfpEndDate
        );
    }

    private Format determineFormat(ConferenceJson conference) {
        // When the conference location OR country is labeled as "Online" or "Virtual", we consider it a virtual event
        if (conference.locationName().equalsIgnoreCase("Online") || conference.locationName().equalsIgnoreCase("Virtual")
                || conference.coordinates().countryName().equalsIgnoreCase("Online") || conference.coordinates().countryName().equalsIgnoreCase("Virtual")) {
            return Format.VIRTUAL;
        }

        return conference.hybrid() ? Format.HYBRID : Format.IN_PERSON;
    }
}