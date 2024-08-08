package apap.tk.stockguard.controller;

import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import apap.tk.stockguard.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @Autowired
    private BarangService barangService;
    
    @GetMapping("/")
//     public String home(Principal principal, Model model) {
//         UserModel user = userService.getUserByUsername(principal.getName());
//         model.addAttribute("user", user);
// //        model.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
//         return "home";
//     }
    public String home(Model model, Principal principal) {
        List<BarangModel> listBarang = barangService.getAllBarang();
        System.out.println("All Barang: " + listBarang);  // Debug output

        List<BarangModel> validBarang = listBarang.stream()
                .filter(b -> b.getKategori() != null)
                .collect(Collectors.toList());
        System.out.println("Valid Barang: " + validBarang);  // Debug output

        // Ambil barang kadaluarsa
        List<BarangModel> barangKadaluarsa = validBarang.stream()
                .filter(b -> b.getTanggalKadaluarsa().isBefore(LocalDateTime.now().plusDays(30)))
                .collect(Collectors.toList());
        System.out.println("Barang Kadaluarsa: " + barangKadaluarsa);  // Debug output

        // Dapatkan rekomendasi tindakan untuk barang kadaluarsa
        Map<BarangModel, String> rekomendasiTindakanMap = barangService.getRekomendasiTindakanMap(barangKadaluarsa);
        System.out.println("Rekomendasi Tindakan Map: " + rekomendasiTindakanMap);  // Debug output
        // Log data
        rekomendasiTindakanMap.forEach((barang, rekomendasi) ->
                System.out.println("Barang: " + barang.getNama() + ", Rekomendasi: " + rekomendasi)
        );

        // Konversi Map ke List pasangan kunci-nilai
        List<Map.Entry<BarangModel, String>> rekomendasiTindakanList = new ArrayList<>(barangService.getRekomendasiTindakanMap(barangKadaluarsa).entrySet());

        // Data untuk chart
        List<String> kategori = validBarang.stream()
                .map(b -> b.getKategori().getNama())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Kategori: " + kategori);

        List<Integer> jumlahStok = kategori.stream()
                .map(k -> validBarang.stream()
                        .filter(b -> b.getKategori().getNama().equals(k))
                        .mapToInt(BarangModel::getStok)
                        .sum())
                .collect(Collectors.toList());
        System.out.println("Jumlah stok barang yang ada: " + jumlahStok);

        List<Long> jumlahKadaluarsa = kategori.stream()
                .map(k -> validBarang.stream()
                        .filter(b -> b.getKategori().getNama().equals(k) && b.getTanggalKadaluarsa().isBefore(LocalDateTime.now().plusDays(30)))
                        .count())
                .collect(Collectors.toList());
        System.out.println("Jumlah barang kadaluarsa: " + jumlahKadaluarsa);

        UserModel user = userService.getUserByUsername(principal.getName());
        System.out.println(user.getRole());
        if (user.getRole() == UserModel.Role.ADMIN) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("user", user);
        model.addAttribute("validBarang", validBarang);
        model.addAttribute("barangKadaluarsa", barangKadaluarsa);
        model.addAttribute("rekomendasiTindakanMap", rekomendasiTindakanMap);
        model.addAttribute("kategori", kategori);
        model.addAttribute("jumlahStok", jumlahStok);
        model.addAttribute("jumlahKadaluarsa", jumlahKadaluarsa);
        model.addAttribute("rekomendasiTindakanList", rekomendasiTindakanList);
        return "home";
    }

   @GetMapping ("/login")
   public String login() {
       return "login";
   }


//    @GetMapping("/logout")
//     public String logout() {
//         return "redirect:/login?logout";  // Redirect to login page with a logout parameter
//     }

//    @RequestMapping ("/viewalluser")
//    public String user() {
//        return "view-all-user";
//    }
}
