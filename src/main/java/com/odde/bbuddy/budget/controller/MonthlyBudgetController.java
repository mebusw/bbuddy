package com.odde.bbuddy.budget.controller;

import com.odde.bbuddy.budget.domain.MonthlyBudget;
import com.odde.bbuddy.budget.domain.MonthlyBudgetPlanner;
import com.odde.bbuddy.budget.view.PresentableAddMonthlyBudget;
import com.odde.bbuddy.budget.view.PresentableMonthlyBudgetAmount;
import com.odde.bbuddy.common.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.odde.bbuddy.common.Formats.DAY;
import static com.odde.bbuddy.common.controller.Urls.*;

@Controller
@RequestMapping(MONTHLYBUDGETS)
@PropertySource("classpath:resultMessages.properties")
public class MonthlyBudgetController {

    private final MonthlyBudgetPlanner planner;
    private final PresentableMonthlyBudgetAmount presentableMonthlyBudgetAmount;
    private final PresentableAddMonthlyBudget presentableAddMonthlyBudget;
    private final View message;

    @Value("${monthlybudgets.add.success}")
    String successMessage;

    @Value("${monthlybudgets.add.failed}")
    String failedMessage;

    @Autowired
    public MonthlyBudgetController(
            MonthlyBudgetPlanner planner,
            PresentableMonthlyBudgetAmount presentableMonthlyBudgetAmount,
            PresentableAddMonthlyBudget presentableAddMonthlyBudget,
            View<String> message) {
        this.planner = planner;
        this.presentableMonthlyBudgetAmount = presentableMonthlyBudgetAmount;
        this.presentableAddMonthlyBudget = presentableAddMonthlyBudget;
        this.message = message;
    }

    @PostMapping(ADD)
    public ModelAndView submitAddMonthlyBudget(
            @Valid @ModelAttribute MonthlyBudget monthlyBudget,
            BindingResult result) {
        if (!result.hasFieldErrors())
            planner.addMonthlyBudget(monthlyBudget)
                    .success(() -> message.display(successMessage))
                    .failed(() -> message.display(failedMessage));
        return addMonthlyBudget();
    }

    @GetMapping(ADD)
    public ModelAndView addMonthlyBudget() {
        return presentableAddMonthlyBudget;
    }

    @GetMapping(TOTALAMOUNT)
    public ModelAndView totalAmountOfMonthlyBudget(
            @RequestParam("startDate") @DateTimeFormat(pattern = DAY) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = DAY) LocalDate endDate) {
        presentableMonthlyBudgetAmount.display(planner.getAmount(startDate, endDate));
        return presentableMonthlyBudgetAmount;
    }

}
