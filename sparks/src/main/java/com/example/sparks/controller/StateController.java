package com.example.sparks.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.sparks.Entities.DistrictPlan;
import com.example.sparks.Entities.DistrictPlanMetrics;
import com.example.sparks.Entities.SeatShareData;
import com.example.sparks.Entities.State;
import com.example.sparks.Entities.StateSummary;
import com.example.sparks.Repositories.StateRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@RequestMapping(path="/state") // This means URL's start with /state (after Application path)
@CrossOrigin
public class StateController {
    @Autowired // This means to get the bean called stateRepository
            // Which is auto-generated by Spring, we will use it to handle the data
    private StateRepository stateRepository;

    // @TODO: delete this
    @PostMapping(path="/add-test-state") // Map ONLY POST Requests
    public @ResponseBody String addTestState() {
        State s = new State();

        // dummy data
        JSONObject testGeoJson = new JSONObject();
        testGeoJson.put("value1", 1);
        JSONObject temp = new JSONObject();
        int[] tempArray = {1, 2, 3};
        temp.put("array", tempArray);
        testGeoJson.put("value2", temp);

        s.setGeoJson(testGeoJson.toString());
        s.setStateCode("NV");

        stateRepository.save(s);

        // confirmation message sent to requester
        return "Saved Test State";
    }

    // @TODO clean this up and document
    @GetMapping(path="/{stateCode}")
    public @ResponseBody StateSummary getStateByCode(@PathVariable String stateCode) {
        State state = stateRepository.findByStateCode(stateCode).get(0);

        // populate StateSummary Object to be returned
        StateSummary summary = new StateSummary();
        List<DistrictPlanMetrics> metricList = new ArrayList<DistrictPlanMetrics>();
        for (DistrictPlan plan: state.getDistrictPlans()) {
            // map.put(plan.getId(), plan.getName());
            metricList.add(plan.createMetrics());
        }
        summary.setDistrictPlanMetrics(metricList);

        return summary;
    }

    // @TODO: delete this
    @GetMapping(path="/session")
    public @ResponseBody String getStateFromSession(@RequestBody MultiValueMap<String, String> values) {

        return values.getFirst("stateCode");
    }

    // @TODO: maybe returns geojson with statecode?
    @GetMapping(path="/all")
    public @ResponseBody Iterable<State> getAllStates() {
        // This returns a JSON with the states
        return stateRepository.findAll();
    }


    // @TODO: DISTRICT ENDPOINT
    // get seat share data using statecode and district plan id
    @GetMapping(path="/district/seat-share/{districtPlanId}")
    public @ResponseBody SeatShareData getSeatShareData(@PathVariable Long districtPlanId, 
                                                    @RequestBody MultiValueMap<String, String> values) {
        // List<State> states = stateRepository.findByStateCode(values.getFirst("stateCode"));
        State state = stateRepository.findByStateCode(values.getFirst("stateCode")).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);

        return districtPlan.createSeatShare();
    }

    // @TODO: DISTRICT ENDPOINT
    // get district stats by statecode and district plan id
    @GetMapping(path="/district/{districtPlanId}")
    public @ResponseBody DistrictPlanMetrics getDistrictSummary(@PathVariable Long districtPlanId, 
                                                                @RequestBody MultiValueMap<String, String> values) {
        // @TODO
        State state = stateRepository.findByStateCode(values.getFirst("stateCode")).get(0);
        DistrictPlan districtPlan = state.getDistrictPlanById(districtPlanId);

        return districtPlan.createMetrics();
    }

}