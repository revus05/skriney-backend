package com.example.skrineybackend.grpc;

import com.example.grpc.CreateTransactionRequest;
import com.example.grpc.CreateTransactionResponse;
import com.example.grpc.TransactionServiceGrpc;
import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.service.TransactionService;
import com.example.skrineybackend.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
public class TransactionGrpcService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final TransactionService transactionService;
    private final UserService userService;

    @Override
    public void createTransaction(CreateTransactionRequest request, StreamObserver<CreateTransactionResponse> responseObserver) {
        User user = userService.getUserByTelegramId(request.getTelegramId());

        CreateTransactionRequestDTO dto = new CreateTransactionRequestDTO(
                new BigDecimal(request.getAmount()),
                user.getSettings().getDefaultCurrency(),
                null,
                user.getSettings().getDefaultBankAccount().getUuid(),
                request.getCategoryUuid()
        );

        TransactionDTO transaction = transactionService.createTransaction(dto, user.getUuid());

        CreateTransactionResponse response = CreateTransactionResponse.newBuilder().setTransactionUuid(transaction.getUuid()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
