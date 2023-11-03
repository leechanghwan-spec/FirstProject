package com.school.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
import com.school.demo.object.Chargers;
import com.school.demo.object.Members;
import com.school.demo.object.Reservations;
import com.school.demo.request.AddChargerRequest;
import com.school.demo.request.UserSetRequest;
import com.school.demo.service.ChargerService;
import com.school.demo.service.ReservationService;
import com.school.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private UserService userService;
	private ChargerService chargerService;
	private ReservationService reservationService;
	@Autowired
	public AdminController (UserService userService, ChargerService chargerService, ReservationService reservationService) {
		this.userService = userService;
		this.chargerService = chargerService;
		this.reservationService=reservationService;
		}
	
	@GetMapping("/main")
	public String adminPage(@RequestParam(defaultValue = "1") int page,
	                        @RequestParam(defaultValue = "20") int size,
	                        Model model) {

	    List<Members> members = userService.getUsers();
	    model.addAttribute("members", members);

	    List<Chargers> chargers = chargerService.getChargers(page, size);
	    model.addAttribute("chargers", chargers);

	    List<Reservations> reservations = userService.getReservations();
	    model.addAttribute("reservations", reservations);

	    // 현재 페이지 번호와 한 페이지당 보여줄 데이터 개수(size)도 모델에 추가
	    model.addAttribute("page", page);
	    model.addAttribute("size", size);
	    
	    // 총 데이터 개수를 가져오는 메소드가 있다고 가정하고 totalPages 계산
	    int totalChargersCount = chargerService.getTotalCount();  // getTotalCount()는 총 데이터 개수를 반환하는 메소드입니다.
	                                                               // 해당 메소드가 없다면 적절한 방법으로 총 데이터 개수를 구해야 합니다.
	    
		int totalPages = (int)Math.ceil((double)totalChargersCount / size);
		model.addAttribute("totalPages", totalPages);

		return "admin";
	}

    
    @GetMapping("/user")
    public String adminUserPage() {
    	return "user";
    }
    
    @GetMapping("/addcharger")
    public String adminChargersPage() {
    	return "addcharger";
    }
    
    @PostMapping("/usersetting")
    public String deleteUser(@ModelAttribute UserSetRequest request) {
    	String username = request.getUsername();
    	String action = request.getAction();
    	if(action.equals("Delete")){
    		if(userService.deleteUser(username)==1)
    			return "redirect:/admin/main";
    	}
    	else if(action.equals("SetAdmin")){
    		if(userService.setAdmin(username)==1)
    			return "redirect:/admin/main";
    	}
    	else if(action.equals("SetUser")) {
    		if(userService.setUser(username)==1)
    			return "redirect:/admin/main";
    	}
		return "redirect:/admin/main";
    	}
    
    @PostMapping("/deletecharger")
    public String deleteCharger(int no) {
    	return "";
    }
    
    @PostMapping("/deletereservation")
    public String deleteReservation(@RequestParam int id) {
    	if(reservationService.deleteReservation(id) == ResultCode.SUCCESS ) return "redirect:/admin/main";
    	else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "삭제 실패");
    }
}