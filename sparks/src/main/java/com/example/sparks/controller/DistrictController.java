// package com.example.sparks.controller;

// import javax.servlet.http.HttpSession;

// import com.example.sparks.Entities.DistrictPlan;
// import com.example.sparks.Repositories.DistrictPlanRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// // @TODO: SPRING DOESNT SEE THIS FOR SOME REASON
// @Controller // This means that this class is a Controller
// @RequestMapping(path="/district") // This means URL's start with /demo (after Application path)
// @CrossOrigin
// public class DistrictController {
//     @Autowired // This means to get the bean called userRepository
//          // Which is auto-generated by Spring, we will use it to handle the data
//     private DistrictPlanRepository districtPlanRepository;

//     // @TODO: delete this
//     @PostMapping(path = "/add-test-plan")
//     public String addTestDistrictPlan() {
//         DistrictPlan plan1 = new DistrictPlan();
//         plan1.setCompactness(.3);
//         plan1.setEfficiencyGap(.6);

//         DistrictPlan plan2 = new DistrictPlan();
//         plan2.setCompactness(.7);
//         plan2.setEfficiencyGap(.1);

//         districtPlanRepository.save(plan1);
//         districtPlanRepository.save(plan2);

//         return "2 plans added";
//     }
// }
