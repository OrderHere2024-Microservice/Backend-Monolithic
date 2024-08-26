package com.backend.orderhere.repository;

import com.backend.orderhere.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
  List<UserAddress> findAllByUserId(Integer userId);

  Optional<UserAddress> findFirstByUserIdAndIsDefault(Integer userId, boolean isDefault);

  Optional<UserAddress> findByUserAddressId(Integer userAddressId);

  void deleteByUserAddressId(Integer userAddressId);


  @Transactional
  @Modifying
  @Query("UPDATE UserAddress ua SET ua.isDefault = false WHERE ua.userId = ?1")
  void setAllIsDefaultToFalseForUser(Integer userId);

  @Query("SELECT ua FROM UserAddress ua WHERE ua.userId = ?1 ORDER BY ua.updatedTime DESC")
  Optional<UserAddress> findLatestUpdatedAddressByUserId(Integer userId);

  @Query("SELECT COUNT(ua) FROM UserAddress ua WHERE ua.userId = ?1 AND ua.isDefault = true")
  Long countDefaultAddressesForUser(Integer userId);
}
