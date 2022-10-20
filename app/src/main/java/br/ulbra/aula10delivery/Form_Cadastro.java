package br.ulbra.aula10delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.ktx.Firebase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_Cadastro extends AppCompatActivity {
    private CircleImageView fotoUsuario;
    private Button bt_cadastrar, bt_selecionarFoto;
    private EditText edit_nome, edit_email, edit_senha;
    private TextView txt_mensagemErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        iniciarComponentes();

        edit_nome.addTextChangedListener(cadastroTextWatcher);
        edit_email.addTextChangedListener(cadastroTextWatcher);
        edit_senha.addTextChangedListener(cadastroTextWatcher);

        bt_cadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CadastrarUsuario(v);
            }
        });
    }

    public void CadastrarUsuario(View view)
    {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Snackbar snackbar = Snackbar.make(view,"Cadastro realizado com sucesso",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                    snackbar.show();
                }
                else
                {
                    String erro;

                    try
                    {
                        throw task.getException();
                    }
                    catch(FirebaseAuthWeakPasswordException e)
                    {
                        erro = "Coloque uma senha com no minimo 6 caracteres";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        erro = "Email invalido";
                    }
                    catch(FirebaseAuthUserCollisionException e)
                    {
                        erro = "Usuario já cadastrado";
                    }
                    catch(FirebaseNetworkException e)
                    {
                        erro = "Sem conexão";
                    }
                    catch(Exception e)
                    {
                        erro = e.getMessage();
                    }

                    txt_mensagemErro.setText(erro);
                }
            }
        });
    }

    public void iniciarComponentes() {
        fotoUsuario = findViewById(R.id.fotoUsuario);

        bt_selecionarFoto = findViewById(R.id.bt_selecionarFoto);

        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);

        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);

    }

    TextWatcher cadastroTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {
                bt_cadastrar.setEnabled(true);
                bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.dark_red));
            } else {
                bt_cadastrar.setEnabled(false);
                bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}