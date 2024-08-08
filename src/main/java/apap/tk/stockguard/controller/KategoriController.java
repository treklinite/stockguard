package apap.tk.stockguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.service.KategoriService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/kategori")
public class KategoriController {
    @Autowired
    private KategoriService kategoriService;

    @GetMapping("")
    public String viewAllKategori(Model model) {
        List<KategoriModel> listKategori = kategoriService.getKategoriList();
        model.addAttribute("listKategori", listKategori);
        return "view-all-kategori";
    }

    @GetMapping("/add")
    public String addKategoriFormPage(Model model) {
        model.addAttribute("kategori", new KategoriModel());
        return "form-add-kategori";
    }

    @PostMapping("/add")
    public String addKategoriSubmit(
            @ModelAttribute KategoriModel kategori,
            RedirectAttributes redirectAttributes) {
        try {
            kategoriService.addKategori(kategori);
            redirectAttributes.addFlashAttribute("message", "Kategori berhasil ditambahkan");
            return "redirect:/kategori";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/kategori/add";
        }
    }

    @GetMapping("/update/{id}")
    public String updateKategoriFormPage(
        @PathVariable Integer id,
        Model model
    ) {
        KategoriModel kategori = kategoriService.getKategoriById(id);
        model.addAttribute("kategori", kategori);
        return "form-update-kategori";
    }

    @PostMapping("/update")
    public String updateKategoriSubmit(
            @ModelAttribute KategoriModel kategori,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (kategoriService.isExistExceptCurrent(kategori.getId(), kategori.getNama())) {
                throw new IllegalArgumentException("Nama kategori sudah ada. Silakan gunakan nama lain.");
            }
                kategoriService.updateKategori(kategori);
                redirectAttributes.addFlashAttribute("message", "Kategori berhasil di-update");
                return "redirect:/kategori";
        } catch (IllegalArgumentException e) {
            System.out.println("Catch IllegalArgumentException");
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/kategori/update/" + kategori.getId();
        } catch (Exception e){
            System.out.println("Catch exception");
            redirectAttributes.addFlashAttribute("errorMessage", "Kategori tidak berhasil di-update");
            return "redirect:/kategori/update/" + kategori.getId();

        }
    }

    @GetMapping("/delete/{id}")
    public String deleteKategori(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        KategoriModel kategori = kategoriService.getKategoriById(id);
        if (kategori != null) {
            kategoriService.deleteKategori(kategori);
            redirectAttributes.addFlashAttribute("message", "Kategori berhasil dihapus");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Kategori tidak ditemukan");
        }
        return "redirect:/kategori";
    }
}
