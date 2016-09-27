package com.niit.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niit.shoppingcart.dao.CategoryDAO;
import com.niit.shoppingcart.dao.ProductDAO;
import com.niit.shoppingcart.dao.SupplierDAO;
import com.niit.shoppingcart.model.Category;
import com.niit.shoppingcart.model.Product;
import com.niit.shoppingcart.model.Supplier;
import com.niit.shoppingcart.util.FileUtil;

public class ProductController {


	@Autowired(required = true)
	private Product product;
	
	@Autowired(required = true)
	private ProductDAO productDAO;
	
	@Autowired(required = true)
	private Category category;
	
	@Autowired(required = true)
	private CategoryDAO categoryDAO;
	
	@Autowired(required = true)
	private Supplier supplier;
	
	@Autowired(required = true)
	private SupplierDAO supplierDAO;
	
	private String path = "D:\\shoppingcart\\img";
	
	@RequestMapping(value = "/prouct", method = RequestMethod.GET)
	public String listProducts(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("product", new Category());
		model.addAttribute("supplier", new Supplier());
		model.addAttribute("productList", this.productDAO.list());
		model.addAttribute("categoryList", this.categoryDAO.list());
		model.addAttribute("supplierList", this.supplierDAO.list());
		return "product";
	}
	//for add update product both
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute("product")Product product){
		
		Category category = categoryDAO.getByName(product.getCategory().getName());
		//supplierDAO.saveOrUpdate(category); //why to save?
		
		Supplier Supplier = supplierDAO.getByName(product.getSupplier().getName());
		//supplierDAO.saveOrUpdate(supplier);//why to save?
		
		product.setCategory(category);
		product.setSupplier(Supplier);
		
		//product.setCategoryId(category.getId());
		//product.setSupplierId(Supplier.getId());
		productDAO.update(product);
		
		//MultipartFile file = product.getImageurl();
		
		//MultipartFile file = null;
		FileUtil.upload(path,file,product.getId()+".jpg");
		return "product";
		//return "redirect:/uploadFile";
	}
	
	@RequestMapping("product/remove/{productid}")
	public String removeProduct(@PathVariable("id") Product id, ModelMap model) throws Exception{
		
		try{
			productDAO.delete(id);
			model.addAttribute("message","Successfully Added");
			
		}catch (Exception e){
			model.addAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		//redirectAttrs.addFlashAttribute(arg0,arg1)
		return "redirect:/products";
	}
	
	@RequestMapping("product/edit/{productid}")	
	public String editProduct(@PathVariable("id") String id, Model model){
		System.out.println("editproduct");
		model.addAttribute("product", this.productDAO.get(id));
		model.addAttribute("listproduct", this.productDAO.list());
		model.addAttribute("categoryList", this.categoryDAO.list());
		model.addAttribute("supplierList", this.supplierDAO.list());
		return "product";
		
		}
		
	@RequestMapping("product/get/{productid}")	
	public String getProduct(@PathVariable("id") String id, Model model,RedirectAttributes redirectAttributes) {
			redirectAttributes.addFlashAttribute("selectedProduct",productDAO.get(id));
	return "product";
		
		}
	
	@RequestMapping(value = "/backToHome", method = RequestMethod.GET)
	public String backToHome(@ModelAttribute("selectedProduct")
	final Object selectedProduct, final Model model){
		
		model.addAttribute("selectedProduct", selectedProduct);
		//model.addAttribute("categoryList", this.categoryDAO.list())
		
		return "/home";

	}
	
}
