package pjatk.zadanie_tbk_5_3.demo.CartesianPoint;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartesianPointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getAllPointsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/points")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void createPointStatusCode201() throws Exception {
        CartesianPoint point = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point)))
                .andExpect(status().isCreated());
    }

    @Test
    void createPointCheckValues() throws Exception {
        CartesianPoint point = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].x", is(1)))
                .andExpect(jsonPath("$[0].y", is(2)));
    }

    @Test
    void createPointInvalidType() throws Exception {
        String someVeryRandomString = "Kalafior";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(someVeryRandomString)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllPointsTwoPointsStatusCodeAndCheckValues() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);
        CartesianPoint point2 = new CartesianPoint(3, 4);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].x", is(1)))
                .andExpect(jsonPath("$[0].y", is(2)))
                .andExpect(jsonPath("$[1].x", is(3)))
                .andExpect(jsonPath("$[1].y", is(4)));
    }

    @Test
    void getNPointsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/points/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void getNPointsZeroOutOfOne() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getNPointsNegative() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/-12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getNPointsInvalidType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/aaa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getNPointsOneOutOfTwo() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);
        CartesianPoint point2 = new CartesianPoint(3, 4);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].x", is(1)))
                .andExpect(jsonPath("$[0].y", is(2)));
    }

    @Test
    void getNPointsTwoOutOfTwo() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);
        CartesianPoint point2 = new CartesianPoint(3, 4);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].x", is(1)))
                .andExpect(jsonPath("$[0].y", is(2)))
                .andExpect(jsonPath("$[1].x", is(3)))
                .andExpect(jsonPath("$[1].y", is(4)));
    }

    @Test
    void deletePointNonExistent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/points/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deletePointOne() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/points/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deletePointInvalidType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/points/aaa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deletePointNoId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/points/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsInvalidType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/aaa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsFirstNegativeValue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/-1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsSecondNegativeValue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsEmptyParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance//")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsMissingFirstPoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance//1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsMissingSecondPoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsNonExistentPoint() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getDistanceBetweenPointsStatusCode() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);
        CartesianPoint point2 = new CartesianPoint(3, 4);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getDistanceBetweenPointsCheckValue() throws Exception {
        CartesianPoint point1 = new CartesianPoint(1, 2);
        CartesianPoint point2 = new CartesianPoint(3, 4);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(point2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/points/distance/1/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(2.8284271247461903)));
    }
}