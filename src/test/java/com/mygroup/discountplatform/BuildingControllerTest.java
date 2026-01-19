package com.mygroup.discountplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.discountplatform.controllers.BuildingController;
import com.mygroup.discountplatform.dtos.BuildingByCityDTO;
import com.mygroup.discountplatform.dtos.BuildingCreateRequestDTO;
import com.mygroup.discountplatform.dtos.BuildingListDTO;
import com.mygroup.discountplatform.dtos.BuildingResponseDTO;
import com.mygroup.discountplatform.services.BuildingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuildingController.class)
class BuildingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    @MockitoBean
    private BuildingService buildingService;

    @Nested
    @DisplayName("GET /buildings")
    class FindAllBuildingsTests {

        @Test
        @DisplayName("Without city param - should return all buildings as BuildingListDTO")
        void findAll_WithoutCityParam_ShouldReturnAllBuildingsAsBuildingListDTO() throws Exception {
            List<BuildingListDTO> buildings = List.of(
                new BuildingListDTO("Tower A", "123 Main St", "Madrid"),
                new BuildingListDTO("Tower B", "456 Oak Ave", "Barcelona")
            );
            when(buildingService.findAll()).thenReturn(buildings);

            mockMvc.perform(get("/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Tower A")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].city", is("Madrid")))
                .andExpect(jsonPath("$[1].name", is("Tower B")))
                .andExpect(jsonPath("$[1].city", is("Barcelona")));

            verify(buildingService).findAll();
            verify(buildingService, never()).findByCity(any());
        }

        @Test
        @DisplayName("Without city param when no buildings exist - should return empty list")
        void findAll_WithoutCityParam_WhenNoBuildingsExist_ShouldReturnEmptyList() throws Exception {
            when(buildingService.findAll()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

            verify(buildingService).findAll();
        }

        @Test
        @DisplayName("With city param - should return filtered buildings as BuildingByCityDTO")
        void findAll_WithCityParam_ShouldReturnFilteredBuildingsAsBuildingByCityDTO() throws Exception {
            List<BuildingByCityDTO> buildings = List.of(
                new BuildingByCityDTO("Tower A", "123 Main St"),
                new BuildingByCityDTO("Tower C", "789 Pine Rd")
            );
            when(buildingService.findByCity("Madrid")).thenReturn(buildings);

            mockMvc.perform(get("/buildings").param("city", "Madrid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Tower A")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].city").doesNotExist())
                .andExpect(jsonPath("$[1].name", is("Tower C")));

            verify(buildingService).findByCity("Madrid");
            verify(buildingService, never()).findAll();
        }

        @Test
        @DisplayName("With city param when no buildings in city - should return empty list")
        void findAll_WithCityParam_WhenNoBuildingsInCity_ShouldReturnEmptyList() throws Exception {
            when(buildingService.findByCity("NonExistentCity")).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/buildings").param("city", "NonExistentCity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

            verify(buildingService).findByCity("NonExistentCity");
        }

        @Test
        @DisplayName("With blank city param - should return all buildings")
        void findAll_WithBlankCityParam_ShouldReturnAllBuildings() throws Exception {
            List<BuildingListDTO> buildings = List.of(
                new BuildingListDTO("Tower A", "123 Main St", "Madrid")
            );
            when(buildingService.findAll()).thenReturn(buildings);

            mockMvc.perform(get("/buildings").param("city", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city", is("Madrid")));

            verify(buildingService).findAll();
            verify(buildingService, never()).findByCity(any());
        }

        @Test
        @DisplayName("With empty city param - should return all buildings")
        void findAll_WithEmptyCityParam_ShouldReturnAllBuildings() throws Exception {
            List<BuildingListDTO> buildings = List.of(
                new BuildingListDTO("Tower A", "123 Main St", "Madrid")
            );
            when(buildingService.findAll()).thenReturn(buildings);

            mockMvc.perform(get("/buildings").param("city", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city", is("Madrid")));

            verify(buildingService).findAll();
            verify(buildingService, never()).findByCity(any());
        }
    }

    @Nested
    @DisplayName("POST /buildings")
    class CreateBuildingTests {

        @Nested
        @DisplayName("Happy Path")
        class HappyPath {

            @Test
            @DisplayName("With valid data - should return 201 CREATED")
            void createBuilding_WithValidData_ShouldReturnCreatedBuilding() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "123 Main St", "Madrid");
                BuildingResponseDTO response = new BuildingResponseDTO(
                    LocalDateTime.now(), LocalDateTime.now(), 1L, "Tower A", "123 Main St", "Madrid"
                );
                when(buildingService.createBuilding(any(BuildingCreateRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("With valid data - should return correct response fields")
            void createBuilding_WithValidData_ShouldReturnCorrectResponseFields() throws Exception {
                LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "123 Main St", "Madrid");
                BuildingResponseDTO response = new BuildingResponseDTO(now, now, 1L, "Tower A", "123 Main St", "Madrid");
                when(buildingService.createBuilding(any(BuildingCreateRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Tower A")))
                    .andExpect(jsonPath("$.address", is("123 Main St")))
                    .andExpect(jsonPath("$.city", is("Madrid")))
                    .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                    .andExpect(jsonPath("$.updatedAt", is(notNullValue())));
            }

            @Test
            @DisplayName("With valid data - should call service once")
            void createBuilding_WithValidData_ShouldCallServiceOnce() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "123 Main St", "Madrid");
                BuildingResponseDTO response = new BuildingResponseDTO(
                    LocalDateTime.now(), LocalDateTime.now(), 1L, "Tower A", "123 Main St", "Madrid"
                );
                when(buildingService.createBuilding(any(BuildingCreateRequestDTO.class))).thenReturn(response);

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

                verify(buildingService, times(1)).createBuilding(any(BuildingCreateRequestDTO.class));
            }
        }

        @Nested
        @DisplayName("Name Validation")
        class NameValidation {

            @Test
            @DisplayName("With null name - should return bad request")
            void createBuilding_WithNullName_ShouldReturnBadRequest() throws Exception {
                String json = """
                    {"address": "123 Main St", "city": "Madrid"}
                    """;

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With empty name - should return bad request")
            void createBuilding_WithEmptyName_ShouldReturnBadRequest() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("", "123 Main St", "Madrid");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @ParameterizedTest
            @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
            @DisplayName("With blank name - should return bad request")
            void createBuilding_WithBlankName_ShouldReturnBadRequest(String blankName) throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO(blankName, "123 Main St", "Madrid");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }
        }

        @Nested
        @DisplayName("Address Validation")
        class AddressValidation {

            @Test
            @DisplayName("With null address - should return bad request")
            void createBuilding_WithNullAddress_ShouldReturnBadRequest() throws Exception {
                String json = """
                    {"name": "Tower A", "city": "Madrid"}
                    """;

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With empty address - should return bad request")
            void createBuilding_WithEmptyAddress_ShouldReturnBadRequest() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "", "Madrid");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @ParameterizedTest
            @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
            @DisplayName("With blank address - should return bad request")
            void createBuilding_WithBlankAddress_ShouldReturnBadRequest(String blankAddress) throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", blankAddress, "Madrid");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }
        }

        @Nested
        @DisplayName("City Validation")
        class CityValidation {

            @Test
            @DisplayName("With null city - should return bad request")
            void createBuilding_WithNullCity_ShouldReturnBadRequest() throws Exception {
                String json = """
                    {"name": "Tower A", "address": "123 Main St"}
                    """;

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With empty city - should return bad request")
            void createBuilding_WithEmptyCity_ShouldReturnBadRequest() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "123 Main St", "");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @ParameterizedTest
            @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
            @DisplayName("With blank city - should return bad request")
            void createBuilding_WithBlankCity_ShouldReturnBadRequest(String blankCity) throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("Tower A", "123 Main St", blankCity);

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }
        }

        @Nested
        @DisplayName("Error Cases")
        class ErrorCases {

            @Test
            @DisplayName("With all fields blank - should return multiple validation errors")
            void createBuilding_WithAllFieldsBlank_ShouldReturnMultipleValidationErrors() throws Exception {
                BuildingCreateRequestDTO request = new BuildingCreateRequestDTO("   ", "   ", "   ");

                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With empty request body - should return bad request")
            void createBuilding_WithEmptyRequestBody_ShouldReturnBadRequest() throws Exception {
                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With null request body - should return bad request")
            void createBuilding_WithNullRequestBody_ShouldReturnBadRequest() throws Exception {
                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With invalid JSON - should return bad request")
            void createBuilding_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                    .andExpect(status().isBadRequest());

                verify(buildingService, never()).createBuilding(any());
            }

            @Test
            @DisplayName("With wrong content type - should return unsupported media type")
            void createBuilding_WithWrongContentType_ShouldReturnUnsupportedMediaType() throws Exception {
                mockMvc.perform(post("/buildings")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("name=Tower A&address=123 Main St&city=Madrid"))
                    .andExpect(status().isUnsupportedMediaType());

                verify(buildingService, never()).createBuilding(any());
            }
        }
    }
}
