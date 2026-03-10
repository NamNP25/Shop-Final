package com.example.Shop.service;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    // 1. Lấy giỏ hàng từ Session (Đảm bảo luôn trả về Map)
    @SuppressWarnings("unchecked")
    public Map<Long, CartItem> getCart(HttpSession session) {
        Object cartObj = session.getAttribute("cart");

        // Nếu session cũ là List (lỗi), xóa đi để tạo mới Map
        if (cartObj != null && !(cartObj instanceof Map)) {
            session.removeAttribute("cart");
            cartObj = null;
        }

        if (cartObj == null) {
            Map<Long, CartItem> cart = new HashMap<>();
            session.setAttribute("cart", cart);
            return cart;
        }
        return (Map<Long, CartItem>) cartObj;
    }

    // 2. Hàm THÊM VÀO GIỎ (Sửa lỗi "cannot find symbol" của bạn)
    public void addToCart(Long productId, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);

        if (cart.containsKey(productId)) {
            // Nếu đã có trong giỏ, tăng số lượng lên 1
            CartItem item = cart.get(productId);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            // Nếu chưa có, lấy thông tin từ DB và thêm mới
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setImage(product.getImage());
            newItem.setQuantity(1);

            cart.put(productId, newItem);
        }
        // Lưu lại vào session
        session.setAttribute("cart", cart);
    }

    // 3. Cập nhật số lượng (Dùng cho AJAX ở Controller)
    public CartItem updateQuantity(Long productId, Integer qty, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(productId)) {
            CartItem item = cart.get(productId);
            if (qty <= 0) {
                cart.remove(productId);
                return null;
            }
            item.setQuantity(qty);
            session.setAttribute("cart", cart);
            return item;
        }
        return null;
    }

    // 4. Xóa khỏi giỏ
    public void removeFromCart(Long productId, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute("cart", cart);
    }
}