package gift.product.controller;

import gift.option.dto.OptionResponse;
import gift.option.service.OptionService;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductViewController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String name,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        Page<ProductResponse> products = productService.search(name, pageable);
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest("", null, ""));
        return "productAddEdit";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("productRequest") ProductRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productAddEdit";
        }
        productService.create(request);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductResponse response = productService.findById(id);
        ProductRequest request = new ProductRequest(response.name(), response.price(), response.imgUrl());
        model.addAttribute("productRequest",request);
        model.addAttribute("productId", id);
        return "productAddEdit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("productRequest") ProductRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "productAddEdit";
        }

        productService.update(id,request);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        ProductResponse product = productService.findById(productId);
        List<OptionResponse> options = optionService.getOptions(productId);

        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "product/productDetail";
    }

    @PostMapping("/{productId}/options")
    public String addOption(
            @PathVariable Long productId,
            @RequestParam String name,
            @RequestParam int quantity
    ) {
        optionService.addOption(productId, name, quantity);
        return "redirect:/admin/products/" + productId;
    }
}

