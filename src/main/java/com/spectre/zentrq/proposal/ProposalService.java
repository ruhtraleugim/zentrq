package com.spectre.zentrq.proposal;
import com.spectre.zentrq.proposal.dto.*;
import java.util.List;
public interface ProposalService {
    ProposalResponse submit(Long professionalId, CreateProposalRequest request);
    List<ProposalResponse> listByJob(Long jobId, Long clientId);
    List<ProposalResponse> listByProfessional(Long professionalId);
    AcceptProposalResponse accept(Long proposalId, Long clientId);
    ProposalResponse reject(Long proposalId, Long clientId);
}
