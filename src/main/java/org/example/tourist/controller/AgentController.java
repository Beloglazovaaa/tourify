package org.example.tourist.controller;

import org.example.tourist.models.Agent;
import org.example.tourist.services.AgentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * Показать список всех агентов.
     *
     * @param model модель для представления
     * @return название представления "agents"
     */
    @GetMapping
    public String listAgents(Model model) {
        List<Agent> agents = agentService.getAllAgents();
        model.addAttribute("agents", agents);
        return "agents";
    }

    /**
     * Показать страницу создания нового агента.
     *
     * @param model модель для представления
     * @return название представления "agent-form"
     */
    @GetMapping("/create")
    public String createAgentPage(Model model) {
        model.addAttribute("agent", new Agent());
        return "agent-form";
    }

    /**
     * Создать нового агента.
     *
     * @param agent        объект агента из формы
     * @param isAvailable  флаг доступности агента
     * @return перенаправление на список агентов
     */
    @PostMapping("/create")
    public String createAgent(@ModelAttribute Agent agent, @RequestParam(required = false) Boolean isAvailable) {
        agent.setIsAvailable(isAvailable != null && isAvailable);
        agentService.addAgent(agent);
        return "redirect:/agents";
    }

    /**
     * Показать страницу редактирования агента.
     *
     * @param id    ID агента
     * @param model модель для представления
     * @return название представления "agent-form"
     */
    @GetMapping("/edit/{id}")
    public String editAgentPage(@PathVariable Long id, Model model) {
        Agent agent = agentService.getAgentById(id);
        model.addAttribute("agent", agent);
        return "agent-form";
    }

    /**
     * Обновить данные агента.
     *
     * @param id           ID агента
     * @param agent        обновленный объект агента из формы
     * @param isAvailable  флаг доступности агента
     * @return перенаправление на список агентов
     */
    @PostMapping("/edit/{id}")
    public String updateAgent(@PathVariable Long id, @ModelAttribute Agent agent, @RequestParam(required = false) Boolean isAvailable) {
        agent.setIsAvailable(isAvailable != null && isAvailable);
        agentService.updateAgent(id, agent);
        return "redirect:/agents";
    }

    /**
     * Удалить агента по ID.
     *
     * @param id ID агента
     * @return перенаправление на список агентов
     */
    @PostMapping("/delete/{id}")
    public String deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return "redirect:/agents";
    }
}
