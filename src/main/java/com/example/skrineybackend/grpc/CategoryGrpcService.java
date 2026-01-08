package com.example.skrineybackend.grpc;

import com.example.grpc.Category;
import com.example.grpc.CategoryServiceGrpc;
import com.example.grpc.GetCategoriesRequest;
import com.example.grpc.GetCategoriesResponse;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.service.CategoryService;
import com.example.skrineybackend.service.UserService;
import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CategoryGrpcService extends CategoryServiceGrpc.CategoryServiceImplBase {

  private final CategoryService categoryService;
  private final UserService userService;

  @Override
  public void getCategories(
      GetCategoriesRequest request, StreamObserver<GetCategoriesResponse> responseObserver) {
    User user = userService.getUserByTelegramId(request.getTelegramId());

    List<Category> categories =
        categoryService.getCategories(user.getUuid()).stream()
            .map(
                dto ->
                    Category.newBuilder()
                        .setUuid(dto.getUuid())
                        .setTitle(dto.getTitle())
                        .setEmoji(dto.getEmoji())
                        .setCreatedAt(String.valueOf(dto.getCreatedAt()))
                        .setUpdatedAt(String.valueOf(dto.getUpdatedAt()))
                        .build())
            .toList();

    GetCategoriesResponse response =
        GetCategoriesResponse.newBuilder().setStatus(true).addAllCategories(categories).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
