package com.contactlistapp.security.service;


import com.contactlistapp.domain.User;
import com.contactlistapp.exception.ResourceNotFoundException;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // injection with @AllArgsConstructor
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException
                        (String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email)));
        return new UserDetailsImpl(user);
        // returning new object from UserDetailsImpl class (there is a hidden constructor there)

    }
}
