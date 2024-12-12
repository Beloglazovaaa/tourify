package org.example.tourist.services;


import org.example.tourist.models.Agent;
import org.example.tourist.repositories.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /**
     * Получить всех агентов.
     *
     * @return список агентов
     */
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    /**
     * Получить агента по ID.
     *
     * @param id ID агента
     * @return агент
     * @throws RuntimeException если агент не найден
     */
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Агент не найден"));
    }

    /**
     * Добавить нового агента.
     *
     * @param agent объект агента
     * @return сохраненный агент
     */
    public Agent addAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    /**
     * Обновить данные агента.
     *
     * @param id            ID агента
     * @param updatedAgent  обновленный объект агента
     * @return обновленный агент
     * @throws RuntimeException если агент не найден
     */
    public Agent updateAgent(Long id, Agent updatedAgent) {
        Agent agent = getAgentById(id);
        agent.setName(updatedAgent.getName());
        agent.setPhoneNumber(updatedAgent.getPhoneNumber());
        agent.setIsAvailable(updatedAgent.getIsAvailable());
        agent.setImageUrl(updatedAgent.getImageUrl());
        return agentRepository.save(agent);
    }

    /**
     * Удалить агента по ID.
     *
     * @param id ID агента
     */
    public void deleteAgent(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new RuntimeException("Агент не найден");
        }
        agentRepository.deleteById(id);
    }
}
