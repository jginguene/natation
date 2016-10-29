
create table Groupe(
    id integer primary key autoincrement, 
    Nom varchar2(200) not null,
    description varchar2(1000),
    CONSTRAINT nom_unique UNIQUE (Nom )
);




create table assn(
    id integer primary key autoincrement,
    code varchar2(200) not null,
    description varchar2(200) not null
  
);

insert into assn(id,code,description) values (1,'SN1','Savoir nager 1');
insert into assn(id,code,description) values (2,'SN2','Savoir nager 2');
insert into assn(id,code,description) values (3,'ASSN','Attestation scolaire du savoir nager');

create table niveau(
    id integer primary key autoincrement,
    Nom varchar2(200) not null,
    assn_id integer,
    FOREIGN KEY(assn_id) REFERENCES assn(id)  ,    
    CONSTRAINT nom_unique UNIQUE (Nom )
);

insert into niveau (nom,assn_id) values (1,null);
insert into niveau (nom,assn_id) values (2,1);
insert into niveau (nom,assn_id) values (3,2);



create table classe(
    id integer primary key autoincrement,
    nom varchar2(200) not null,
    niveau_id integer(200) not null,
    FOREIGN KEY(niveau_id) REFERENCES niveau(id)  
);

insert into classe(id,nom, niveau_id) values (1,'CP',1);
insert into classe(id,nom,niveau_id) values (2,'CE1',2);
insert into classe(id,nom, niveau_id) values (3,'CE2',2);
insert into classe(id,nom, niveau_id) values (4,'CM1',3);
insert into classe(id,nom, niveau_id) values (5,'CM2',3);
insert into classe(id,nom, niveau_id) values (6,'6ème',3);


create table domaine(
    id integer primary key autoincrement,
    Nom varchar2(200) not null,
    CONSTRAINT nom_unique UNIQUE (Nom)
);

create table competence(
    id integer primary key autoincrement,
    num integer,
    description varchar2(1000),
    niveau_id integer not null,
    domaine_id integer not null, 
    capacite_id integer,    
    FOREIGN KEY(domaine_id) REFERENCES domaine(id),
    FOREIGN KEY(niveau_id) REFERENCES Niveau(id)
);


create table capacite(
    id integer primary key autoincrement,
    Nom varchar2(200) not null,
    description varchar2(1000),
    CONSTRAINT nom_unique UNIQUE (Nom)
);

create table capacite_competence_r(
    capacite_id integer ,
    competence_id integer ,
    FOREIGN KEY(capacite_id) REFERENCES capacite(id),
    FOREIGN KEY(competence_id) REFERENCES competence(id)
);

create table Eleve(
    id integer primary key autoincrement, 
    Nom varchar2(200)  not null,
    Prenom varchar2(200) not null,
    Groupe_Id integer ,
    capactite_Id integer ,
    classe_id integer,
    FOREIGN KEY(Groupe_Id) REFERENCES Groupe(id),
    FOREIGN KEY(capactite_Id) REFERENCES Capacite(id),
    FOREIGN KEY(classe_id) REFERENCES Classe(id)
);


create table eleve_competence_r(
     eleve_Id integer ,
     competence_Id integer ,
     FOREIGN KEY(competence_Id) REFERENCES competence(id),
     FOREIGN KEY(eleve_Id) REFERENCES eleve(id)
);


insert into Groupe(nom,description ) values ("A","Petit bassin - Petite profondeur");
insert into Groupe(nom,description ) values ("B","Petit bassin - Moyenne profondeur");
insert into Groupe(nom,description ) values ("C","Grand bassin - Moyenne profondeur");
insert into Groupe(nom,description ) values ("D","Grand bassin - Grande profondeur");



--insert into niveau (nom) values (4);

CREATE INDEX idx_domaine_id ON domaine (id);
CREATE INDEX idx_niveau_id ON niveau (id);
CREATE INDEX idx_groupe_id ON groupe (id);
CREATE INDEX idx_eleve_groupe ON eleve (Groupe_Id);
CREATE INDEX idx_eleve_capacite ON eleve (capactite_Id);
CREATE INDEX idx_eleve_id ON eleve (id);

CREATE INDEX idx_cap_comp_1 ON capacite_competence_r (capacite_id);
CREATE INDEX idx_cap_comp_2 ON capacite_competence_r (competence_id);

CREATE INDEX idx_elev_comp_1 ON eleve_competence_r (eleve_Id);
CREATE INDEX idx_elev_comp_2 ON eleve_competence_r (competence_id);



--Remplissage d'exemple pour démo

        

insert into domaine(id,nom) values (1,"Entrée dans l'eau");
insert into domaine(id,nom) values (2,"Equilibre");
insert into domaine(id,nom) values (3,"Immersion");
insert into domaine(id,nom) values (4,"Respiration");
insert into domaine(id,nom) values (5,"Propulsion");

insert into  competence(description,num,domaine_id,niveau_id)
values ('Entrer dans l''eau par les escaliers sans s''immerger seul ou à plusieurs.',1,1,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Entrer dans l''eau par l''échelle sans s''immerger.',2,1,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter du bord du petit bassin avec l''aide d''une perche.',3,1,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter du bord.',4,1,1);


insert into  competence(description,num,domaine_id,niveau_id)
values ('Faire le tour du petit bassin.',1,2,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Prendre appui sur le bord et laisser remonter son corps.',2,2,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('S''allonger à l''aide de matériel sur le ventre et sur le dos.',3,2,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('S''allonger sur le ventre et sur le dos.',4,2,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Faire l''étoile de mer pendant 5 secondes.',5,2,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Immerger progressivement son corps jusqu''au menton.',1,3,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Immerger progressivement la tête.',2,3,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('S''immerger et ouvrir les yeux.',3,3,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Ramasser un objet au fond de l''eau là où l''enfant à pied.',4,3,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Souffler dans l''eau avec la bouche.',1,4,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Souffler dans l''eau avec le nez.',2,4,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Souffler dans l''eau avec la bouche ouverte en immersion totale.',3,4,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Marcher en avant, en arrière en se tenant au bord.',1,5,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Marcher en avant, en arrière sans se tenir au bord.',2,5,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Courir dans le petit bassin.',3,5,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Se déplacer avec deux frites en effectuant des battements.',4,5,1);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter du bord du petit bassin en attrapant la perche.',1,1,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter, remonter seul et attraper la perche pour revenir au bord.',2,1,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter avec du matériel.',3,1,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter et revenir sans aide.',4,1,2);


insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer une glissée ventrale avec matériel.',1,2,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer une glissée ventrale sans matériel.',2,2,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer une glissée dorsale avec matériel.',3,2,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer une glissée dorsale sans matériel.',4,2,2);


insert into  competence(description,num,domaine_id,niveau_id)
values ('Descendre sous l''eau le long d''une perche et remonter un objet (petite profondeur).',1,3,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Descendre sous l''eau le long d''une échelle.',2,3,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Passer dans un cerceau immergé et lesté la tête la première.',3,3,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Ramasser un objet à 1 m de profondeur départ assis.',4,3,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter en moyennement profondeur, se déplacer sous l''eau, se laisser flotter et regagner le bord.',5,3,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Souffler dans l''eau avec la bouche et le nez en immersion totale.',1,4,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Maîtriser la durée de l''expiration.',2,4,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Maîtriser la répétition des expirations sous l''eau en statique.',3,4,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Se déplacer avec du matériel en effectuant des battements.',1,5,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Se déplacer sur le dos avec du matériel en effectuant des battements.',2,5,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Départ sauté, effectuer 12 m avec matériel dans le petit bassin en grande profondeur.',3,5,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Départ sauté, effectuer 12 m sans matériel dans le petit bassin en grande profondeur.',4,5,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Départ sauté, effectuer 15 m sans matériel.',5,5,2);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Sauter dans le grand bain, assis ou debout.',1,1,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Entrée dans l''eau en chute arrière.',2,1,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Plonger à genou.',3,1,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Plonger sans élan.',4,1,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer un demi-tour sans reprise d''appui.',1,2,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer une glissée ventrale ou dorsale suivie de mouvements propulsifs.',2,2,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Réaliser un surplace en position verticale pendant 15 s.',3,2,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Réaliser un surplace en position horizontale pendant 15 s.',3,2,3);


insert into  competence(description,num,domaine_id,niveau_id)
values ('Ramasser un objet au fond de l''eau à une profondeur supérieure à celle de l''enfant.',1,3,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Se déplacer en immersion sous un obstacle  d''1,5 m.',2,3,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Effectuer un plongeon canard.',3,3,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Ramasser un objet au fond de l''eau à une profondeur de 2 m.',4,3,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Maîtriser la répétition des expirations sous l''eau en dynamique.',1,4,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Rester immerger en statique 10 s.',2,4,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Se déplacer sur 25 m en coordonnant les mouvements des jambes et des bras avec une ceinture.',1,5,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Départ au bord du bassin, se déplacer sur 25 m, effectuer un virage, une coulée et une reprise de nage pour gagner le bord.',2,5,3);

insert into  competence(description,num,domaine_id,niveau_id)
values ('Départ sauté ou plongé, se déplacer 15 m sur le ventre, effectuer un demi-tour, se déplacer 15 m sur le dos.',3,5,3);


insert into capacite(id,nom ) values (1,"Coquillage");
insert into capacite(id,nom ) values (2,"Etoile de mer");
insert into capacite(id,nom ) values (3,"Canard");
insert into capacite(id,nom ) values (4,"Crocodile");
insert into capacite(id,nom ) values (5,"Poisson");
insert into capacite(id,nom ) values (6,"Tortue");
insert into capacite(id,nom ) values (7,"Salamandre");
insert into capacite(id,nom ) values (8,"Loutre de mer");



insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Immerger progressivement la tête.'
and  nom = 'Coquillage';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Marcher en avant, en arrière sans se tenir au bord.'
and  nom = 'Coquillage';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Faire l''étoile de mer pendant 5 secondes.'
and  nom = 'Etoile de mer';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Ramasser un objet au fond de l''eau là où l''enfant à pied.'
and  nom = 'Etoile de mer';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Ramasser un objet à 1 m de profondeur départ assis.'
and  nom = 'Crocodile';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Départ sauté, effectuer 12 m sans matériel dans le petit bassin en grande profondeur.'
and  nom = 'Crocodile';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Départ sauté, effectuer 12 m avec matériel dans le petit bassin en grande profondeur.'
and  nom = 'Canard';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Départ sauté, effectuer 15 m sans matériel.'
and  nom = 'Poisson';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Sauter en moyennement profondeur, se déplacer sous l''eau, se laisser flotter et regagner le bord.'
and  nom = 'Poisson';



insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Entrée dans l''eau en chute arrière.'
and  nom = 'Salamandre';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Effectuer un demi-tour sans reprise d''appui.'
and  nom = 'Salamandre';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Réaliser un surplace en position verticale pendant 15 s.'
and  nom = 'Salamandre';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Se déplacer en immersion sous un obstacle  d''1,5 m.'
and  nom = 'Salamandre';


insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Départ au bord du bassin, se déplacer sur 25 m, effectuer un virage, une coulée et une reprise de nage pour gagner le bord.'
and  nom = 'Loutre de mer';

insert into capacite_competence_r (competence_id,capacite_id)
select competence.id,capacite.id from competence, capacite  where 
competence.description = 'Départ sauté ou plongé, se déplacer 15 m sur le ventre, effectuer un demi-tour, se déplacer 15 m sur le dos.'
and  nom = 'Salamandre';




insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Aaricia", "ANDRE", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Aaron", "ARNAUD", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abbon", "AUBERT", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abby", "AUBRY", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abdel", "BAILLY", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abdon", "BARBIER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abel", "BARON", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abélard", "BARTHELEMY", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abélia", "BENOIT", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abella", "BERGER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abigaël", "BERNARD", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abondance", "BERTRAND", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abraham", "BESSON", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Abriel", "BLANC", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Acace", "BLANCHARD", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Achahildis", "BONNET", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Achille", "BOUCHER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Acmé", "BOURGEOIS", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Ada", "BOUVIER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adalard", "BOYER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adalbert", "BRETON", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adalric", "BRIAND", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adalsinde", "BRUN", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adam", "BRUNET", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adama", "CARON", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adélaïde", "CARRE", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adélaïs", "CHARLES", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adélard", "CHARPENTIER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adèle", "CHEVALIER", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adelice", "CLEMENT", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adélie", "CLERC", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adelin", "COLIN", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adelind", "COLLET", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adeline", "COLLIN", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adelphe", "COUSIN", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adelphine", "DANIEL", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adeltrude", "DAVID", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Adémar", "DENIS", 1 ,1);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Babet", "DESCHAMPS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Babeth", "DUBOIS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Babette", "DUFOUR", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Babylas", "DUMAS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bacchus", "DUMONT", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Balbine", "DUPONT", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Baldwin", "DUPUIS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Balthazar", "DUPUY", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Baptiste", "DURAND", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Baptistin", "DUVAL", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Baptistine", "FABRE", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barban", "FAURE", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barbara", "FLEURY", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barbe", "FONTAINE", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barberine", "FOURNIER", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barnabé", "FRANCOIS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barnard", "GAILLARD", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barry", "GALL", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barthélemy", "GARNIER", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bartholomé", "GAUTHIER", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bartholomée", "GAUTIER", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Barthy", "GAY", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bartimée", "GERARD", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bartolo", "GERMAIN", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bartoumiéu", "GILLET", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Basile", "GIRARD", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Basilissa", "GIRAUD", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Basilisse", "GOFF", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Basso", "GRAND", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bassus", "GROS", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bastian", "GUERIN", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Bastiane", "GUICHARD", 2,2);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cadfan", "GUILLAUME", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cadroe", "GUILLOT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cainnech", "GUILLOU", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Caïssa", "GUYOT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Caïus", "HAMON", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cajetan", "HENRY", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Caleb", "HERVE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calimero", "HUBERT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Caline", "HUET", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calixta", "HUMBERT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calixte", "JACQUET", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calixtine", "JEAN", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Callie", "JOLY", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calliope", "JULIEN", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Callista", "LACROIX", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calliste", "LAMBERT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Callistine", "LANGLOIS", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calogero", "LAURENT", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calvin", "LECLERC", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Calypso", "LECOMTE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Camélia", "LEFEBVRE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cameron", "LEFEVRE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Camiho", "LEGRAND", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Camilla", "LEMAIRE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Camille", "LEMOINE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Camilo", "LEROUX", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Candice", "LEROY", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Candide", "LOUIS", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Candy", "LUCAS", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Canice", "MARCHAL", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cannelle", "MARCHAND", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cantien", "MARIE", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Cantienne", "MASSON", 3,3);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dagmar", "MATHIEU", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dahlia", "MERCIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daisy", "MEUNIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dalia", "MICHEL", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daliane", "MILLET", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dalila", "MONNIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damaris", "MOREAU", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damase", "MOREL", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damian", "MORIN", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damiane", "MOULIN", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damien", "NICOLAS", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damienne", "NOEL", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Damon", "OLIVIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dana", "OLLIVIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danaé", "PARIS", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danaël", "PELLETIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danick", "PERRIN", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daniè", "PERROT", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daniel", "PETIT", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daniela", "PHILIPPE", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danièle", "PICARD", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danielle", "PIERRE", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Daniset", "POIRIER", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Danitza", "PREVOST", 4,4);
insert into Eleve(prenom,nom, groupe_id,classe_id) values ("Dany", "RENARD", 4,4);


