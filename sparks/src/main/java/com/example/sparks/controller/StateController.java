package com.example.sparks.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sparks.entity.District;
import com.example.sparks.entity.DistrictPlan;
import com.example.sparks.entity.State;
import com.example.sparks.enumerable.PoliticalGroup;
import com.example.sparks.nonentity.BoxAndWhiskerResponse;
import com.example.sparks.nonentity.DistrictPlanCompareMetrics;
import com.example.sparks.nonentity.DistrictPlanMetrics;
import com.example.sparks.nonentity.SeatShareData;
import com.example.sparks.nonentity.SeawulfSummary;
import com.example.sparks.nonentity.StateSummary;
import com.example.sparks.repository.DistrictRepository;
import com.example.sparks.repository.StateRepository;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
// @RequestMapping(path="/state") // This means URL's start with /state (after Application path)
@CrossOrigin
public class StateController {
    @Autowired // This means to get the bean called stateRepository
               // Which is auto-generated by Spring, we will use it to handle the data
    private StateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    /**
     * "/state/{stateCode}"
     * @param stateCode two letter String representation of the requested state
     * @return StateSummary object with data to be displayed by the frontend GUI
     */
    @GetMapping(path="/state/{stateCode}")
    public @ResponseBody StateSummary getStateByCode(@PathVariable String stateCode) {
        State state = stateRepository.findByStateCode(stateCode).get(0);

        // populate StateSummary Object to be returned
        StateSummary summary = new StateSummary();
        List<DistrictPlanMetrics> metrics = new ArrayList<DistrictPlanMetrics>();
        for (DistrictPlan plan: state.getDistrictPlans()) {
            metrics.add(plan.createMetrics());
        }
        summary.setDistrictPlanMetricsList(metrics);

        return summary;
    }

    // get seat share data using statecode and district plan id
    @GetMapping(path="/district/seat-share/{stateCode}/{districtPlanId}")
    public @ResponseBody SeatShareData getSeatShareData(@PathVariable Long districtPlanId, 
    @PathVariable String stateCode) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);

        return districtPlan.generateSeatShareData();
    }

    // get district stats by statecode and district plan id
    @GetMapping(path="/district/{stateCode}/{districtPlanId}")
    public @ResponseBody DistrictPlanMetrics getDistrictSummary(@PathVariable Long districtPlanId, 
    @PathVariable String stateCode) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);
        return districtPlan.createMetrics();
    }

    // get box and whisker data needed
    @GetMapping(path="/district/box-whisker/{stateCode}/{districtPlanId}")
    public @ResponseBody BoxAndWhiskerResponse getBoxAndWhisker(@PathVariable String stateCode,
    @PathVariable Long districtPlanId) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);
        BoxAndWhiskerResponse response = new BoxAndWhiskerResponse();
        response.setDistrictData(districtPlan.generateBoxAndWhiskerData());
        response.setBoxAndWhiskerData(state.createSeawulfBoxAndWhiskerMap());
        response.calculateError();
        return response;
    }

    // get interesting seawulf data
    @GetMapping(path="/state/seawulf/{stateCode}")
    public @ResponseBody SeawulfSummary getSeawulfSummary(@PathVariable String stateCode) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        return state.createSeawulfSummary();
    }

    // get plan compare statistics
    @GetMapping(path="/district/compare/{stateCode}/{districtPlanId1}/{districtPlanId2}")
    public @ResponseBody DistrictPlanCompareMetrics getDistrictPlanCompareMetrics(@PathVariable String stateCode,
    @PathVariable Long districtPlanId1, @PathVariable Long districtPlanId2) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        DistrictPlan plan1 = state.getDistrictPlanById(districtPlanId1);
        DistrictPlan plan2 = state.getDistrictPlanById(districtPlanId2);

        DistrictPlanCompareMetrics compareMetrics = new DistrictPlanCompareMetrics();
        compareMetrics.setCompactness1(plan1.getCompactness());
        compareMetrics.setEfficiencyGap1(plan1.generateEfficiencyGap());
        compareMetrics.setMeanPopulationDeviation1(plan1.generateMeanPopulationDeviation());
        compareMetrics.setNumIncumbentSafeDistricts1(plan1.generateIncumbentSafeDistrictsMap().size());
        compareMetrics.setNumCombinedMajorityMinorityDistricts1(plan1.generateNumberCombinedMajorityMinorityDistricts());
        compareMetrics.setCompactness2(plan2.getCompactness());
        compareMetrics.setEfficiencyGap2(plan2.generateEfficiencyGap());
        compareMetrics.setMeanPopulationDeviation2(plan2.generateMeanPopulationDeviation());
        compareMetrics.setNumIncumbentSafeDistricts2(plan2.generateIncumbentSafeDistrictsMap().size());
        compareMetrics.setNumCombinedMajorityMinorityDistricts2(plan2.generateNumberCombinedMajorityMinorityDistricts());
        return compareMetrics;
    }

    // update district population data
    @PutMapping(path="/district/update")
    public @ResponseBody String updateDistrictPopulationData() {
        Iterable<State> allStates = stateRepository.findAll();

        for (State state: allStates) {
            for (DistrictPlan plan: state.getDistrictPlans()) {
                for (District district: plan.getDistricts()) {
                    Map<PoliticalGroup, Integer> populationMetrics = new HashMap<PoliticalGroup, Integer>();
                    for(PoliticalGroup group: PoliticalGroup.values()) {
                        populationMetrics.put(group, district.generatePopulation(group));
                    }
                    district.setPopulationMetrics(populationMetrics);
                    districtRepository.save(district);
                }
            }
        }
        return "done";
    }

    // get all state geojsons
    @GetMapping(path="/state/geojson/all")
    public @ResponseBody Map<String, JSONObject> getAllStateGeoJson() {
        Map<String, JSONObject> stateCodeToGeoJsonMap = new HashMap<String, JSONObject>();
        
        JSONParser parser = new JSONParser();
        Iterable<State> states = stateRepository.findAll();
        for (State state: states) {
            String stateCode = state.getStateCode();
            try {
                FileReader reader = new FileReader("./src/main/java/com/example/sparks/data/" + stateCode + "/state.json");
                Object object = parser.parse(reader);
                JSONObject geoJson = (JSONObject) object;
                stateCodeToGeoJsonMap.put(stateCode, geoJson);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return stateCodeToGeoJsonMap;
    }

    // get geoJSON for specific district plan
    @GetMapping(path="district/geojson/{stateCode}/{districtPlanId}")
    public @ResponseBody JSONObject getDistrictPlanGeoJson(@PathVariable String stateCode,
    @PathVariable Long districtPlanId) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        String planName = state.getDistrictPlanById(districtPlanId).getName();

        JSONObject response = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("./src/main/java/com/example/sparks/data/" + stateCode + "/"
            + planName + ".json");
            Object object = parser.parse(reader);
            response = (JSONObject) object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return response;
    }

    // get district level population metrics for a district plan
    @GetMapping(path="district/population-metrics/{stateCode}/{districtPlanId}")
    public @ResponseBody List<Map<PoliticalGroup, Integer>> getDistrictPopulationMetrics(@PathVariable String stateCode,
    @PathVariable Long districtPlanId) {
        State state = stateRepository.findByStateCode(stateCode).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);
        List<Map<PoliticalGroup,Integer>> response = new ArrayList<Map<PoliticalGroup,Integer>>();

        for (District district: districtPlan.getDistricts()) {
            Map<PoliticalGroup,Integer> tempPopulationMap = new HashMap<PoliticalGroup,Integer>();
            for (PoliticalGroup group: PoliticalGroup.values()) {
                tempPopulationMap.put(group, district.getPopulationData(group));
            }
            response.add(tempPopulationMap);
        }

        return response;
    }





    // @TODO
    @GetMapping(path="/district/test/{stateCode}/{districtPlanId}")
    public @ResponseBody DistrictPlan getDistrictPlanTest(@PathVariable String stateCode, @PathVariable Long districtPlanId) {
        return stateRepository.findByStateCode(stateCode).get(0).getDistrictPlanById(districtPlanId);
    }

    @GetMapping(path="/pwd")
    public @ResponseBody String getPwd() {
        return StateController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}