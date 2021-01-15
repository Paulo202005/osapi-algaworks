package com.algaworks.osapi.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.osapi.api.model.Comentario;
import com.algaworks.osapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osapi.domain.exception.NegocioException;
import com.algaworks.osapi.domain.model.Cliente;
import com.algaworks.osapi.domain.model.OrdemServico;
import com.algaworks.osapi.domain.model.StatusOrdemServico;
import com.algaworks.osapi.domain.repository.ClienteRepository;
import com.algaworks.osapi.domain.repository.ComentarioRepository;
import com.algaworks.osapi.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {			
		OrdemServico ordemServico = buscar(ordemServicoId);
						
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}
		
	public Comentario adicionarComentario(Long ordemSevicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemSevicoId);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
	}
}
