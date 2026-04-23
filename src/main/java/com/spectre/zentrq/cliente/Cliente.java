package com.spectre.zentrq.cliente;

import com.spectre.zentrq.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@DiscriminatorValue("CLIENTE")
@NoArgsConstructor
public class Cliente extends User {
}