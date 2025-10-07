package view;
import controller.CarteleraController;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import model.Pelicula;


/**
 *
 * @author renatoaravena
 */
public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    
    //Instancias
    private CarteleraController controller;
    private DefaultTableModel dtm;
   
    
    public MainFrame() {
        initComponents();
        
        controller = new CarteleraController();
                
        //Crear modelo de la tabla con columnas estandar
        dtm = new DefaultTableModel(new Object[]{"ID", "Titulo", "Director", "Año", "Duracion", "Genero"}, 0){
        
            //Evitar edicion en tablas
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            };
            
        };
        
        //Asignamos la tabla por defecto al JTable
        table.setModel(dtm);
        
        //Propiedad de celda seleccionable
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Al seleccionar una fila, cargar formulario.
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow(); //Fila pinchada
            
            if(r >= 0){
                Integer id = (Integer) dtm.getValueAt(r, 0); //Fila 0
                String titulo = (String) dtm.getValueAt(r, 1);
                String director = (String) dtm.getValueAt(r, 2);
                Integer anio = (Integer) dtm.getValueAt(r, 3);
                Integer duracion = (Integer) dtm.getValueAt(r, 4);
                String genero = (String) dtm.getValueAt(r, 5);
                
                cargarFormulario(id, titulo, director, anio, duracion, genero);
                
                
            }
        
        });
        
        //Cargamos años al filtro
        cargarAnios();
        
        refrescarTabla();
        
    }
    
    
    //Metodos 
    
    private void cargarFormulario(Integer id, String titulo, String director, Integer anio, Integer duracion, String genero){
    
        txtId.setText(id != null ? id.toString() : "");
        txtTitulo.setText(titulo != null ? titulo : "");
        txtDirector.setText(director != null ? director : "");
        txtAnio.setText(anio != null ? anio.toString() : "");
        txtDuracion.setText(duracion != null ? duracion.toString() : "");
        
       if(genero != null && !genero.isEmpty()){
           cbGenero.setSelectedItem(genero); 
       }else{
           cbGenero.setSelectedIndex(0);// "Seleccione género", porque es el indice 0 de mi cbox
        }
        

    }
    
    
    private void refrescarTabla(){ 
        try{
            //Obtenemos lista de peliculas
            List<Pelicula> datos = controller.listarTodasLasPeliculas();
            
            aplicarTabla(datos);
        
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error al refrescar: " + e.getMessage());
        
        }
    
        
    }

    
    private void limpiarFormulario(){
        txtId.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        txtAnio.setText("");
        txtDuracion.setText("");
        cbGenero.setSelectedIndex(0);
        
        //Deseleccionar columnas o filas
        table.clearSelection();
        
        cbFiltroGenero.setSelectedIndex(0);
        
        //resetear años
        int añoActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        cbAnioInicio.setSelectedItem("1895");
        cbAnioFin.setSelectedItem(String.valueOf(añoActual));
        
        
    }
    
    
    private String validarFormulario(){
        if(txtTitulo.getText().isBlank()){
            return "El título es obligatorio";
        }
        if(txtDirector.getText().isBlank()){
            return "El director es obligatorio";
        }
        if(txtAnio.getText().isBlank()){
            return "El año es obligatorio";
        }
        if(txtDuracion.getText().isBlank()){
            return "La duración es obligatoria";
        }
        if(cbGenero.getSelectedIndex() == 0){
            return "Debe seleccionar un género";
        }

        // Validaciones adicionales de formato
        try {
            int añoActual = LocalDate.now().getYear();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            if(anio < 1895 || anio > añoActual){
                return "El año debe estar entre 1895 y " + añoActual;
            }
        } catch (NumberFormatException e) {
            return "El año debe ser un número válido";
        }

        try {
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            if(duracion <= 0 || duracion > 500){
                return "La duración debe ser entre 1 y 500 minutos";
            }
        } catch (NumberFormatException e) {
            return "La duración debe ser un número válido";
        }

        return null; // Retorna null si no hay errores
    }
    
    
    private void aplicarBusqueda(){
        String buscar = txtBuscar.getText().trim();
        
        try{
            List<Pelicula> datos = buscar.isBlank() ? controller.listarTodasLasPeliculas() : controller.buscarPeliculasPorTitulo(buscar);
            aplicarTabla(datos);
            
           
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
        
        }
        
        
        
    }
    
    
    private void aplicarTabla(List<Pelicula> datos){
        
        try{
            //Agregar peliculas a la tabla
            dtm.setRowCount(0); //Limpiar filas actuales
            if (datos != null){
                for(Pelicula pelicula : datos){
                    dtm.addRow(new Object[]{pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(), 
                        pelicula.getAnio(), pelicula.getDuracion(), pelicula.getGenero()});
                }
            }
           
            
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error al aplicar a tabla" + e.getMessage());
        }
    
    
    }
    
    
    private void cargarAnios() {
    DefaultComboBoxModel<String> modelInicio = new DefaultComboBoxModel<>();
    DefaultComboBoxModel<String> modelFin = new DefaultComboBoxModel<>();
    
    int añoActual = LocalDate.now().getYear();
    
    // Cargar años desde 1895 hasta el año actual + 5
    for (int i = 1895; i <= añoActual + 5; i++) {
        modelInicio.addElement(String.valueOf(i));
        modelFin.addElement(String.valueOf(i));
    }
    
    // Asignar los modelos a los ComboBox
    cbAnioInicio.setModel(modelInicio);
    cbAnioFin.setModel(modelFin);
    
    // Establecer valores por defecto (1895 como inicio, año actual como fin)
    cbAnioInicio.setSelectedItem("1895");
    cbAnioFin.setSelectedItem(String.valueOf(añoActual));
}
    
    private void aplicarFiltros() {
    try {
        String generoSeleccionado = (String) cbFiltroGenero.getSelectedItem();
        String anioInicioStr = (String) cbAnioInicio.getSelectedItem();
        String anioFinStr = (String) cbAnioFin.getSelectedItem();
        
        // Validar que se hayan seleccionado años
        if (anioInicioStr == null || anioFinStr == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un rango de años válido");
            return;
        }
        
        // Validar rango de años
        int anioInicio = Integer.parseInt(anioInicioStr);
        int anioFin = Integer.parseInt(anioFinStr);
        
        if (anioInicio > anioFin) {
            JOptionPane.showMessageDialog(this, "El año inicial no puede ser mayor al año final");
            return;
        }
        
        List<Pelicula> datos;
        
        // Lógica de filtrado combinado
        if ("Todos".equals(generoSeleccionado)) {
            // Filtrar solo por rango de años
            datos = controller.buscarPeliculasPorRangoAnios(anioInicio, anioFin);
        } else {
            // Filtrar por género y rango de años
            datos = controller.buscarPeliculasPorGeneroYRangoAnios(generoSeleccionado, anioInicio, anioFin);
        }
        
        aplicarTabla(datos);
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error en el formato de los años");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al aplicar filtros: " + e.getMessage());
    }
}
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnRefrescar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnNuevaPeli = new javax.swing.JButton();
        btnAgregarPeli = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtDirector = new javax.swing.JTextField();
        txtTitulo = new javax.swing.JTextField();
        txtDuracion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbGenero = new javax.swing.JComboBox<>();
        cbFiltroGenero = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbAnioInicio = new javax.swing.JComboBox<>();
        cbAnioFin = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cine");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        btnRefrescar.setText("Refrescar");
        btnRefrescar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });
        btnRefrescar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnRefrescarKeyPressed(evt);
            }
        });
        jToolBar1.add(btnRefrescar);
        jToolBar1.add(jSeparator3);

        btnNuevaPeli.setText("Nueva Pelicula");
        btnNuevaPeli.setFocusable(false);
        btnNuevaPeli.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevaPeli.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevaPeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaPeliActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevaPeli);

        btnAgregarPeli.setText("Agregar Pelicula");
        btnAgregarPeli.setFocusable(false);
        btnAgregarPeli.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregarPeli.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarPeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPeliActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregarPeli);

        btnModificar.setText("Modificar Pelicula");
        btnModificar.setFocusable(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnModificar);

        btnEliminar.setText("Eliminar");
        btnEliminar.setFocusable(false);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEliminar);
        jToolBar1.add(jSeparator2);

        jLabel11.setText("Buscar pelicula: ");
        jToolBar1.add(jLabel11);

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });
        jToolBar1.add(txtBuscar);

        btnBuscar.setText("Buscar");
        btnBuscar.setFocusable(false);
        btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBuscar);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.setFocusable(false);
        btnLimpiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLimpiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLimpiar);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jLabel1.setText("ID:");

        jLabel6.setText("Género (*):");

        txtId.setEditable(false);

        txtDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirectorActionPerformed(evt);
            }
        });

        jLabel2.setText("Titulo (*):");

        jLabel3.setText("Director (*):");

        jLabel4.setText("Año (*):");

        jLabel5.setText("Duración (*):");

        cbGenero.setMaximumRowCount(10);
        cbGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione género", "Comedia", "Drama", "Acción", "Ciencia Ficción", "Terror", "Aventura" }));
        cbGenero.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGeneroActionPerformed(evt);
            }
        });

        cbFiltroGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Comedia", "Drama", "Acción", "Ciencia Ficción", "Terror", "Aventura" }));
        cbFiltroGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltroGeneroActionPerformed(evt);
            }
        });

        jLabel7.setText("FIltrar por género:");

        jLabel8.setText("FIltrar por año:");

        cbAnioInicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbAnioInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAnioInicioActionPerformed(evt);
            }
        });

        cbAnioFin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbAnioFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAnioFinActionPerformed(evt);
            }
        });

        jLabel9.setText("Desde:");

        jLabel10.setText("Hasta:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDirector, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(txtAnio)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTitulo)
                            .addComponent(txtId)
                            .addComponent(cbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbFiltroGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbAnioInicio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbAnioFin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTitulo)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDirector))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDuracion)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbGenero)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFiltroGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbAnioInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbAnioFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(201, 201, 201))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_START);

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Titulo", "Director", "Año", "Duración", "Género"
            }
        ));
        jScrollPane1.setViewportView(table);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel1.add(filler1, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1182, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecciona una película de la cartelera");
        } else {
            String error = validarFormulario(); // ✅ Solo se llama UNA vez
        
            if (error == null) { // Si no hay errores
                Pelicula pelicula = new Pelicula();
                pelicula.setId(Integer.valueOf(txtId.getText().trim()));
                pelicula.setTitulo(txtTitulo.getText().trim());
                pelicula.setDirector(txtDirector.getText().trim());
                pelicula.setAnio(Integer.parseInt(txtAnio.getText().trim()));
                pelicula.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                pelicula.setGenero((String) cbGenero.getSelectedItem());
            
                try {
                    if (controller.modificarPelicula(pelicula)) {
                        JOptionPane.showMessageDialog(this, "Película Modificada");
                        refrescarTabla();
                        limpiarFormulario();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al modificar la película");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, error); // ✅ Muestra el mensaje de error

            }
        }    
    
    }//GEN-LAST:event_btnModificarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        aplicarBusqueda();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevaPeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaPeliActionPerformed
        limpiarFormulario();
        refrescarTabla();
        
    }//GEN-LAST:event_btnNuevaPeliActionPerformed

    private void btnAgregarPeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPeliActionPerformed
        if(!txtId.getText().isBlank()){
            JOptionPane.showMessageDialog(this, "Selecciona la opción \"Nueva Película\" e ingresa los datos");
        } else {
            String error = validarFormulario(); // ✅ Solo se llama UNA vez

            if (error == null) {
                Pelicula pelicula = new Pelicula();
                pelicula.setTitulo(txtTitulo.getText().trim());
                pelicula.setDirector(txtDirector.getText().trim());
                pelicula.setAnio(Integer.parseInt(txtAnio.getText().trim()));
                pelicula.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                pelicula.setGenero((String) cbGenero.getSelectedItem());

                try {
                    if (controller.agregarPelicula(pelicula)) {
                        JOptionPane.showMessageDialog(this, "Película agregada");
                        refrescarTabla();
                        limpiarFormulario();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al agregar la película");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, error); // ✅ Muestra el mensaje de error
            }
        }
    }//GEN-LAST:event_btnAgregarPeliActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if(txtId.getText().isBlank()){
            JOptionPane.showMessageDialog(this, "Selecciona una pelicula de la cartelera");
        
        }else{
            
            int op = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar esta pelicula?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
            if (op == JOptionPane.YES_OPTION){
                int id = Integer.valueOf(txtId.getText().trim());
                
                try{
                    controller.eliminarPelicula(id);
                    JOptionPane.showMessageDialog(this, "Pelicula eliminada");
                    refrescarTabla();
                    limpiarFormulario();
                
                              
                }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Error al eliminar pelicula" + e.getMessage());
                    
                }
            }
            
            
        }
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
         refrescarTabla();
         
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        txtBuscar.setText("");
        
        
        limpiarFormulario();
        refrescarTabla();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cbAnioFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAnioFinActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_cbAnioFinActionPerformed

    private void cbAnioInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAnioInicioActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_cbAnioInicioActionPerformed

    private void cbFiltroGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFiltroGeneroActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_cbFiltroGeneroActionPerformed

    private void cbGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGeneroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGeneroActionPerformed

    private void txtDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirectorActionPerformed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
            btnBuscar.requestFocus();
            btnBuscar.doClick();
        }
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void btnRefrescarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRefrescarKeyPressed

    }//GEN-LAST:event_btnRefrescarKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarPeli;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevaPeli;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> cbAnioFin;
    private javax.swing.JComboBox<String> cbAnioInicio;
    private javax.swing.JComboBox<String> cbFiltroGenero;
    private javax.swing.JComboBox<String> cbGenero;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDirector;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
