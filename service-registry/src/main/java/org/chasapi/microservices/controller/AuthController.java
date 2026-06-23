@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("user".equals(username) && "pass".equals(password)) {
            return JwtUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
}