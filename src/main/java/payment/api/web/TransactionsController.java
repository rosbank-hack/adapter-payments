package payment.api.web;

import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment.api.mapper.TransactionMapper;
import payment.api.service.EnrichmentService;
import payment.api.model.CreateTransactionRequest;

@RestController
@RequestMapping(path = "/api/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TransactionsController {

    private EnrichmentService enrichmentService;

    @Autowired
    public TransactionsController(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @PostMapping("/create")
    @ApiResponse(code = 204, message = "No content")
    public ResponseEntity createTransaction(@RequestBody CreateTransactionRequest request) {
        enrichmentService.enrichTransaction(TransactionMapper.toTransactionDto(request));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
