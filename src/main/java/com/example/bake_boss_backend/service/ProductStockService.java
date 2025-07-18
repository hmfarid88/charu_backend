package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bake_boss_backend.entity.ClosingSetup;
import com.example.bake_boss_backend.entity.OrderInfo;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.repository.ClosingSetupRepository;
import com.example.bake_boss_backend.repository.OrderInfoRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ClosingSetupRepository closingSetupRepository;

    public List<ProductStock> getProductDistForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByStatus(year, month, username);
    }

    public List<ProductStock> getSalesProductDistForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductBySalesPerson(year, month, username);
    }

    public List<ProductStock> getDatewiseSoldProductStock(String username, LocalDate startDate, LocalDate endDate) {
        return productStockRepository.findDatewiseSoldProductByUsername(username, startDate, endDate);
    }

    public List<ProductStock> getSalesDatewiseSoldProduct(String username, LocalDate startDate, LocalDate endDate) {
        return productStockRepository.findDatewiseProductBySalesPerson(username, startDate, endDate);
    }

    public List<ProductStock> getAllProductStock(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByUsername(year, month, username);
    }

    public List<ProductStock> getDatewiseProductStock(String username, LocalDate startDate, LocalDate endDate) {
        return productStockRepository.findDatewiseProductByUsername(username, startDate, endDate);
    }

    public List<Object[]> getExistingOrder() {
        return orderInfoRepository.findAllList();
    }

    public List<OrderInfo> getExistingSingleOrder(Long orderId) {
        return orderInfoRepository.findByOrderId(orderId);
    }

    public ClosingSetup saveOrUpdateClosingSetup(LocalDate startDate, LocalDate endDate) {
        Optional<ClosingSetup> existingSetup = closingSetupRepository.findByStartDateAndEndDate(startDate, endDate);

        ClosingSetup closingSetup;
        if (existingSetup.isPresent()) {
            // Update the existing record
            closingSetup = existingSetup.get();
            closingSetup.setStartDate(startDate);
            closingSetup.setEndDate(endDate);
        } else {
            // Create a new record
            closingSetup = new ClosingSetup(0, startDate, endDate);
        }

        // Save the updated or new record
        return closingSetupRepository.save(closingSetup);
    }

    public Double getTotalSoldProductQtyToday() {
        return productStockRepository.findTotalSoldProductQtyToday();
    }

    public ProductStock getProductStockById(Long productId) {
        return productStockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ProductStock not found with ID: " + productId));
    }

    @Transactional
    public ProductStock updateProductSale(Long productId, ProductStock updatedProductStock) {
        Optional<ProductStock> existingProductOpt = productStockRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            ProductStock existingProduct = existingProductOpt.get();
            Double oldQty = existingProduct.getProductQty();
            Double newQty = updatedProductStock.getProductQty();
            Double qtyDifference = newQty - oldQty;
            String oldProductName = existingProduct.getProductName();
            String newProductName = updatedProductStock.getProductName();

            existingProduct.setDate(updatedProductStock.getDate());
            existingProduct.setProductName(updatedProductStock.getProductName());
            existingProduct.setDpRate(updatedProductStock.getDpRate());
            existingProduct.setProductQty(updatedProductStock.getProductQty());
            existingProduct.setCustomer(updatedProductStock.getCustomer());
            existingProduct.setTruckNo(updatedProductStock.getTruckNo());
            existingProduct.setNote(updatedProductStock.getNote());

            if (!oldProductName.equals(newProductName)) {

                Double previousRemainingQty = productStockRepository.findRemainingQtyByProductName(newProductName,
                        productId);
                Double newRemainingQty = previousRemainingQty;
                productStockRepository.updateRemainingQtyByProductName(newProductName, productId, newRemainingQty);
                productStockRepository.increaseRemainingQty(oldProductName, productId, oldQty);
                productStockRepository.reduceRemainingQty(newProductName, productId, newQty);
            }

            if (qtyDifference > 0) {
                productStockRepository.reduceRemainingQty(existingProduct.getProductName(), productId, qtyDifference);
            } else if (qtyDifference < 0) {
                productStockRepository.increaseRemainingQty(existingProduct.getProductName(), productId,
                        Math.abs(qtyDifference));
            }

            return productStockRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }

    @Transactional
    public ProductStock updateProductEntry(Long productId, ProductStock updatedProductStock) {
        Optional<ProductStock> existingProductOpt = productStockRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            ProductStock existingProduct = existingProductOpt.get();
            Double oldQty = existingProduct.getProductQty();
            Double newQty = updatedProductStock.getProductQty();
            Double qtyDifference = newQty - oldQty;

            String oldProductName = existingProduct.getProductName();
            String newProductName = updatedProductStock.getProductName();
            Double oldPprice = existingProduct.getPurchasePrice();
            Double newPprice = updatedProductStock.getPurchasePrice();

            // Handle name change before updating the product name
            if (!oldProductName.equals(newProductName)) {
                productStockRepository.reduceRemainingQty(oldProductName, productId, oldQty);
                productStockRepository.increaseRemainingQty(newProductName, productId, newQty);
                existingProduct.setProductName(newProductName);
            }

            // Handle price update and cost price recalculation
            if (Double.compare(oldPprice, newPprice) != 0) {
                Double newTotalQty = existingProduct.getRemainingQty() + updatedProductStock.getProductQty();
                Double totalValue = (existingProduct.getRemainingQty() * existingProduct.getCostPrice()) +
                        (updatedProductStock.getProductQty() * updatedProductStock.getPurchasePrice());
                Double newCostPrice = totalValue / newTotalQty;
                existingProduct.setCostPrice(newCostPrice);

            }

            // Handle qty difference
            if (qtyDifference > 0) {
                productStockRepository.increaseRemainingQty(oldProductName, productId, qtyDifference);
            } else if (qtyDifference < 0) {
                productStockRepository.reduceRemainingQty(oldProductName, productId, Math.abs(qtyDifference));
            }

            // Now update remaining fields
            existingProduct.setDate(updatedProductStock.getDate());
            existingProduct.setSupplier(updatedProductStock.getSupplier());
            existingProduct.setProductQty(newQty);
            existingProduct.setPurchasePrice(newPprice);
            existingProduct.setRemainingQty(existingProduct.getRemainingQty() + qtyDifference);

            return productStockRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }

    @Transactional
    public ProductStock deleteProductEntry(Long productId) {
        Optional<ProductStock> existingProductOpt = productStockRepository.findById(productId);
        if (existingProductOpt.isPresent()) {
            ProductStock existingProduct = existingProductOpt.get();
            String oldProductName = existingProduct.getProductName();
            Double qtyDifference = existingProduct.getProductQty();
            productStockRepository.reduceRemainingQty(oldProductName, productId, qtyDifference);
            productStockRepository.deleteById(productId);
            return existingProduct;
        }
        throw new RuntimeException("Product with ID " + productId + " not found.");
    }
}
