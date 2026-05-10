package UI;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import UI.Components.*;

public class SignInUI extends JFrame {
    private static final long serialVersionUID = 1L;
    JTextField idField, nameField, userField, emailField, phoneField, dateField;
    JPasswordField passField;

    public SignInUI() {

        setTitle("Manager Registration");
        setSize(520,480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg);

        JPanel card = new RoundedPanel(25, new Color(35,35,50));
        card.setPreferredSize(new Dimension(340,380));
        card.setLayout(null);
        bg.add(card);

        JLabel title = new JLabel(" REGISTER", SwingConstants.CENTER);
        title.setBounds(60,15,220,30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card,"ID:",60);
        idField = addField(card,60);

        addLabel(card,"Name:",95);
        nameField = addField(card,95);

        addLabel(card,"Username:",130);
        userField = addField(card,130);

        addLabel(card,"Password:",165);
        passField = new JPasswordField();
        passField.setBounds(140,165,130,28);
        styleField(passField);

        // 🔥 关键：先设默认隐藏
        passField.setEchoChar('•');

        card.add(passField);

        // 👁 按钮
        JButton eyeBtn = new JButton("👁");
        eyeBtn.setBounds(270,165,30,28);
        eyeBtn.setFocusPainted(false);

        // 🔥 toggle
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char)0) {
                passField.setEchoChar('•'); // hide
            } else {
                passField.setEchoChar((char)0); // show
            }
        });

        card.add(eyeBtn);

        addLabel(card,"Email:",200);
        emailField = addField(card,200);

        addLabel(card,"Phone:",235);
        phoneField = addField(card,235);

        addLabel(card,"Date:",270);
        dateField = addField(card,270);

        JButton regBtn = createButton(" Register",305);
        JButton backBtn = createButton(" Return",340);

        card.add(regBtn);
        card.add(backBtn);

        regBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> {
            new MainUI();
            dispose();
        });

        setVisible(true);
    }

    // =========================
    // 🔥 UI helper
    // =========================

    private void addLabel(JPanel panel,String text,int y){
        JLabel label=new JLabel(text);
        label.setBounds(30,y,120,28);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI",Font.PLAIN,13));
        panel.add(label);
    }

    private JTextField addField(JPanel panel,int y){
        JTextField tf=new JTextField();
        tf.setBounds(140,y,160,28);
        styleField(tf);
        panel.add(tf);
        return tf;
    }

    private void styleField(JTextField tf){
        tf.setBackground(new Color(60,60,80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5,8,5,8));
        tf.setFont(new Font("Segoe UI",Font.PLAIN,13));
    }

    private JButton createButton(String text,int y){
        JButton btn=new ModernButton(text);
        btn.setBounds(30,y,270,30);
        return btn;
    }

    // =========================
    // 🔥 REGISTER LOGIC（改 validation）
    // =========================

    public void register(){

        String id=idField.getText().trim();
        String name=nameField.getText().trim();
        String username=userField.getText().trim();
        String password=new String(passField.getPassword()).trim();
        String email=emailField.getText().trim();
        String phone=phoneField.getText().trim();
        String date=dateField.getText().trim();

        if(id.isEmpty()){ new ModernDialog(this,"ID cannot be empty!"); return;}
        if(name.isEmpty()){ new ModernDialog(this,"Name cannot be empty!"); return;}
        if(username.isEmpty()){ new ModernDialog(this,"Username cannot be empty!"); return;}
        if(password.isEmpty()){ new ModernDialog(this,"Password cannot be empty!"); return;}
        if(email.isEmpty()){ new ModernDialog(this,"Email cannot be empty!"); return;}
        if(phone.isEmpty()){ new ModernDialog(this,"Phone cannot be empty!"); return;}
        if(date.isEmpty()){ new ModernDialog(this,"Date cannot be empty!"); return;}

        if(!id.matches("M\\d{3}")){
            new ModernDialog(this,"ID must be like M001");
            return;
        }

        // 🔥 电话格式：0XX-XXX-XXXX
        if(!phone.matches("0\\d{2}-\\d{3}-\\d{4}")){
            new ModernDialog(this,"Phone format: 0XX-XXX-XXXX");
            return;
        }

        if(!date.matches("\\d{4}-\\d{2}-\\d{2}")){
            new ModernDialog(this,"Date must be YYYY-MM-DD");
            return;
        }

        try(BufferedWriter bw=new BufferedWriter(new FileWriter("manager.txt",true))){

            bw.write(id+","+name+","+username+","+password+","+email+","+phone+","+date);
            bw.newLine();

            new ModernDialog(this,"Manager Registered!");

            new MainUI();
            dispose();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}