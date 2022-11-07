package pizza.product;

import java.util.Map;

public class ProductService {

    private final ProductRepository productRepository;

    //
    // constructors and setup
    //

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //
    // business logic
    //

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(String productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("For id " + productId));
    }

    public Double getTotalPrice(Map<String, Integer> productQuantities) {
        // loop over each map entry (which is productId -> quantity) and map each entry to the product's price
        // multiplied by desired quantity. Then sum all up and that is our total.
        return productQuantities.entrySet().stream()
                .mapToDouble(entry -> {
                    Product product = getProduct(entry.getKey());
                    return product.price * entry.getValue();
                })
                .sum();
    }

    public Product createProduct(Product product) {
        if (productRepository.existsById(product.getProductId())) {
            throw new IllegalStateException();
        }
        return productRepository.save(product);
    }
}
