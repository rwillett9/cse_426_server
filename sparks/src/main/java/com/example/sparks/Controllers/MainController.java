package com.example.sparks.Controllers;

import com.example.sparks.Entities.State;
import com.example.sparks.Repositories.StateRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
// @RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
@CrossOrigin(origins = "http://localhost:8080")
public class MainController {
  @Autowired // This means to get the bean called userRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private StateRepository stateRepository;
  // private SeatShareDataRepository seatShareDataRepository;

  @PostMapping(path="/state/add-test-state") // Map ONLY POST Requests
  public @ResponseBody String addNewUser () {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    // User n = new User();
    // n.setName(name);
    // n.setEmail(email);
    // userRepository.save(n);

    State s = new State();

    JSONObject testGeoJson = new JSONObject();
    testGeoJson.put("value1", 1);
    JSONObject temp = new JSONObject();
    int[] tempArray = {1, 2, 3};
    temp.put("array", tempArray);
    testGeoJson.put("value2", temp);
    s.setGeoJson(testGeoJson.toString());

    stateRepository.save(s);
    return "Saved Test State";
  }

  @GetMapping(path="/state/all")
  public @ResponseBody Iterable<State> getAllUsers() {
    // This returns a JSON or XML with the users
    return stateRepository.findAll();
  }
}