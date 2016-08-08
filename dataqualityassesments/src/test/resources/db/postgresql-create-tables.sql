DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id serial NOT NULL,
--  googleid character varying(255),
  name character varying(255),
  password character varying(255),
--  picture character varying(255),
--  profile character varying(255),
--  statr integer NOT NULL DEFAULT '0',
--  statt integer NOT NULL DEFAULT '0',
--  statd integer NOT NULL DEFAULT '0',
  CONSTRAINT users_pkey PRIMARY KEY (id)
--  UNIQUE(googleid)
);

DROP TABLE IF EXISTS campaign;
CREATE TABLE campaign (
  id serial NOT NULL,
  cname character varying(255),
  cendpoint character varying(255),
  cgraphs character varying(255),
  copened integer,
  CONSTRAINT campaign_pkey PRIMARY KEY (id),
  UNIQUE(cname)
);

INSERT INTO campaign VALUES (1,'DBpedia Evaluation Campaign','http://live.dbpedia.org/sparql','http://live.dbpedia.org',1);

DROP TABLE IF EXISTS classes;
CREATE TABLE classes (
  id serial NOT NULL,
  curi character varying(255),
  cname character varying(255),
  cparent numeric(19,2),
  count_cache numeric(19,2),
  is_leaf boolean,
  CONSTRAINT classes_pkey PRIMARY KEY (id)
);

INSERT INTO classes VALUES
	(0,'http://www.w3.org/2002/07/owl#Thing','owl:Thing',-1,2350906,false),
	(1,'http://dbpedia.org/ontology/BasketballLeague','BasketballLeague',220,0,true),
	(2,'http://dbpedia.org/ontology/LunarCrater','LunarCrater',81,0,true),
	(3,'http://dbpedia.org/ontology/MilitaryPerson','MilitaryPerson',226,0,true),
	(4,'http://dbpedia.org/ontology/TimePeriod','TimePeriod',0,0,false),
	(5,'http://dbpedia.org/ontology/AutomobileEngine','AutomobileEngine',106,0,true),
	(6,'http://dbpedia.org/ontology/Enzyme','Enzyme',315,0,true),
	(7,'http://dbpedia.org/ontology/University','University',275,0,true),
	(8,'http://dbpedia.org/ontology/AnatomicalStructure','AnatomicalStructure',0,4211,false),
	(9,'http://dbpedia.org/ontology/TelevisionShow','TelevisionShow',198,0,true),
	(10,'http://dbpedia.org/ontology/LaunchPad','LaunchPad',229,0,true),
	(11,'http://dbpedia.org/ontology/CyclingLeague','CyclingLeague',220,0,true),
	(12,'http://dbpedia.org/ontology/CurlingLeague','CurlingLeague',220,0,true),
	(13,'http://dbpedia.org/ontology/MusicFestival','MusicFestival',64,0,true),
	(14,'http://dbpedia.org/ontology/Tax','Tax',0,0,true),
	(15,'http://dbpedia.org/ontology/IceHockeyPlayer','IceHockeyPlayer',157,0,true),
	(16,'http://dbpedia.org/ontology/PublicTransitSystem','PublicTransitSystem',323,0,true),
	(17,'http://dbpedia.org/ontology/FootballMatch','FootballMatch',282,0,true),
	(18,'http://dbpedia.org/ontology/MouseGeneLocation','MouseGeneLocation',53,0,true),
	(19,'http://dbpedia.org/ontology/MilitaryConflict','MilitaryConflict',64,0,true),
	(20,'http://dbpedia.org/ontology/FilmFestival','FilmFestival',64,0,true),
	(21,'http://dbpedia.org/ontology/Beverage','Beverage',189,0,false),
	(22,'http://dbpedia.org/ontology/SpaceShuttle','SpaceShuttle',131,0,true),
	(23,'http://dbpedia.org/ontology/Archaea','Archaea',333,0,true),
	(24,'http://dbpedia.org/ontology/HandballPlayer','HandballPlayer',157,0,true),
	(25,'http://dbpedia.org/ontology/Arachnid','Arachnid',300,0,true),
	(26,'http://dbpedia.org/ontology/Park','Park',62,0,true),
	(27,'http://dbpedia.org/ontology/Cricketer','Cricketer',157,0,true),
	(28,'http://dbpedia.org/ontology/FloweringPlant','FloweringPlant',152,0,false),
	(29,'http://dbpedia.org/ontology/TelevisionEpisode','TelevisionEpisode',198,0,true),
	(30,'http://dbpedia.org/ontology/Gnetophytes','Gnetophytes',152,0,true),
	(31,'http://dbpedia.org/ontology/Protein','Protein',315,0,true),
	(32,'http://dbpedia.org/ontology/HumanGeneLocation','HumanGeneLocation',53,0,true),
	(33,'http://dbpedia.org/ontology/SpeedwayTeam','SpeedwayTeam',287,0,true),
	(34,'http://dbpedia.org/ontology/ChristianPatriarch','ChristianPatriarch',114,0,true),
	(35,'http://dbpedia.org/ontology/GovernmentType','GovernmentType',0,0,true),
	(36,'http://dbpedia.org/ontology/Town','Town',57,0,true),
	(37,'http://dbpedia.org/ontology/ReligiousBuilding','ReligiousBuilding',358,0,false),
	(38,'http://dbpedia.org/ontology/PowerStation','PowerStation',229,0,true),
	(39,'http://dbpedia.org/ontology/Name','Name',0,0,false),
	(40,'http://dbpedia.org/ontology/FormulaOneRacer','FormulaOneRacer',157,0,true),
	(41,'http://dbpedia.org/ontology/Conifer','Conifer',152,0,true),
	(42,'http://dbpedia.org/ontology/SpeedwayLeague','SpeedwayLeague',220,0,true),
	(43,'http://dbpedia.org/ontology/VideogamesLeague','VideogamesLeague',220,0,true),
	(44,'http://dbpedia.org/ontology/Company','Company',318,0,false),
	(45,'http://dbpedia.org/ontology/Locomotive','Locomotive',131,0,true),
	(46,'http://dbpedia.org/ontology/Wrestler','Wrestler',157,0,true),
	(47,'http://dbpedia.org/ontology/MotorcycleRacingLeague','MotorcycleRacingLeague',220,0,true),
	(48,'http://dbpedia.org/ontology/Sales','Sales',0,0,true),
	(49,'http://dbpedia.org/ontology/AdultActor','AdultActor',214,0,true),
	(50,'http://dbpedia.org/ontology/GridironFootballPlayer','GridironFootballPlayer',157,0,false),
	(51,'http://dbpedia.org/ontology/SoccerLeague','SoccerLeague',220,0,false),
	(52,'http://dbpedia.org/ontology/TeamMember','TeamMember',157,0,true),
	(53,'http://dbpedia.org/ontology/GeneLocation','GeneLocation',0,0,false),
	(54,'http://dbpedia.org/ontology/RoadJunction','RoadJunction',323,0,true),
	(55,'http://dbpedia.org/ontology/Brain','Brain',8,0,true),
	(56,'http://dbpedia.org/ontology/WomensTennisAssociationTournament','WomensTennisAssociationTournament',282,0,true),
	(57,'http://dbpedia.org/ontology/Settlement','Settlement',336,0,false),
	(58,'http://dbpedia.org/ontology/Software','Software',198,0,false),
	(59,'http://dbpedia.org/ontology/Opera','Opera',129,0,true),
	(60,'http://dbpedia.org/ontology/ControlledDesignationOfOriginWine','ControlledDesignationOfOriginWine',143,0,true),
	(61,'http://dbpedia.org/ontology/President','President',100,0,true),
	(62,'http://dbpedia.org/ontology/ArchitecturalStructure','ArchitecturalStructure',320,0,false),
	(63,'http://dbpedia.org/ontology/TennisPlayer','TennisPlayer',157,0,true),
	(64,'http://dbpedia.org/ontology/Event','Event',0,0,false),
	(65,'http://dbpedia.org/ontology/Band','Band',318,0,true),
	(66,'http://dbpedia.org/ontology/Country','Country',336,0,true),
	(67,'http://dbpedia.org/ontology/BullFighter','BullFighter',157,0,true),
	(68,'http://dbpedia.org/ontology/Fish','Fish',300,0,true),
	(69,'http://dbpedia.org/ontology/Magazine','Magazine',150,0,true),
	(70,'http://dbpedia.org/ontology/Galaxy','Galaxy',245,0,true),
	(71,'http://dbpedia.org/ontology/Manhwa','Manhwa',205,0,true),
	(72,'http://dbpedia.org/ontology/OrganisationMember','OrganisationMember',226,0,false),
	(73,'http://dbpedia.org/ontology/TelevisionSeason','TelevisionSeason',198,0,true),
	(74,'http://dbpedia.org/ontology/LawFirm','LawFirm',44,0,true),
	(75,'http://dbpedia.org/ontology/WaterwayTunnel','WaterwayTunnel',90,0,true),
	(76,'http://dbpedia.org/ontology/Airport','Airport',229,0,true),
	(77,'http://dbpedia.org/ontology/Boxer','Boxer',157,0,true),
	(78,'http://dbpedia.org/ontology/Fern','Fern',152,0,true),
	(79,'http://dbpedia.org/ontology/SoccerPlayer','SoccerPlayer',157,0,true),
	(80,'http://dbpedia.org/ontology/Island','Island',336,0,true),
	(81,'http://dbpedia.org/ontology/NaturalPlace','NaturalPlace',320,0,false),
	(82,'http://dbpedia.org/ontology/CollegeCoach','CollegeCoach',226,0,true),
	(83,'http://dbpedia.org/ontology/HumanGene','HumanGene',133,0,true),
	(84,'http://dbpedia.org/ontology/Muscle','Muscle',8,0,true),
	(85,'http://dbpedia.org/ontology/Stream','Stream',263,0,false),
	(86,'http://dbpedia.org/ontology/Hospital','Hospital',358,0,true),
	(87,'http://dbpedia.org/ontology/Philosopher','Philosopher',226,0,true),
	(88,'http://dbpedia.org/ontology/BiologicalDatabase','BiologicalDatabase',123,0,true),
	(89,'http://dbpedia.org/ontology/Church','Church',37,0,true),
	(90,'http://dbpedia.org/ontology/Tunnel','Tunnel',323,0,false),
	(91,'http://dbpedia.org/ontology/Ginkgo','Ginkgo',152,0,true),
	(92,'http://dbpedia.org/ontology/Valley','Valley',81,0,true),
	(93,'http://dbpedia.org/ontology/Writer','Writer',266,0,true),
	(94,'http://dbpedia.org/ontology/Automobile','Automobile',131,0,true),
	(95,'http://dbpedia.org/ontology/Ideology','Ideology',0,0,true),
	(96,'http://dbpedia.org/ontology/SupremeCourtOfTheUnitedStatesCase','SupremeCourtOfTheUnitedStatesCase',377,0,true),
	(97,'http://dbpedia.org/ontology/CanadianFootballTeam','CanadianFootballTeam',287,0,true),
	(98,'http://dbpedia.org/ontology/RadioStation','RadioStation',379,0,true),
	(99,'http://dbpedia.org/ontology/SoccerManager','SoccerManager',226,0,true),
	(100,'http://dbpedia.org/ontology/Politician','Politician',226,0,false),
	(101,'http://dbpedia.org/ontology/Comedian','Comedian',266,0,true),
	(102,'http://dbpedia.org/ontology/ComicsCreator','ComicsCreator',266,0,true),
	(103,'http://dbpedia.org/ontology/Monarch','Monarch',226,0,true),
	(104,'http://dbpedia.org/ontology/Road','Road',323,0,true),
	(105,'http://dbpedia.org/ontology/PlayboyPlaymate','PlayboyPlaymate',226,0,true),
	(106,'http://dbpedia.org/ontology/Device','Device',0,0,false),
	(107,'http://dbpedia.org/ontology/Volcano','Volcano',81,0,true),
	(108,'http://dbpedia.org/ontology/Newspaper','Newspaper',150,0,true),
	(109,'http://dbpedia.org/ontology/AmericanFootballPlayer','AmericanFootballPlayer',50,0,true),
	(110,'http://dbpedia.org/ontology/AcademicJournal','AcademicJournal',150,0,true),
	(111,'http://dbpedia.org/ontology/Artwork','Artwork',198,0,false),
	(112,'http://dbpedia.org/ontology/VolleyballPlayer','VolleyballPlayer',157,0,true),
	(113,'http://dbpedia.org/ontology/Non-ProfitOrganisation','Non-ProfitOrganisation',318,0,true),
	(114,'http://dbpedia.org/ontology/Cleric','Cleric',226,0,false),
	(115,'http://dbpedia.org/ontology/School','School',275,0,true),
	(116,'http://dbpedia.org/ontology/LightNovel','LightNovel',271,0,true),
	(117,'http://dbpedia.org/ontology/SiteOfSpecialScientificInterest','SiteOfSpecialScientificInterest',320,0,true),
	(118,'http://dbpedia.org/ontology/SnookerPlayer','SnookerPlayer',157,0,false),
	(119,'http://dbpedia.org/ontology/IceHockeyLeague','IceHockeyLeague',220,0,true),
	(120,'http://dbpedia.org/ontology/PersonFunction','PersonFunction',0,0,true),
	(121,'http://dbpedia.org/ontology/MusicalArtist','MusicalArtist',266,0,true),
	(122,'http://dbpedia.org/ontology/PoliticalParty','PoliticalParty',318,0,true),
	(123,'http://dbpedia.org/ontology/Database','Database',0,0,false),
	(124,'http://dbpedia.org/ontology/EthnicGroup','EthnicGroup',0,2294,true),
	(125,'http://dbpedia.org/ontology/BaseballTeam','BaseballTeam',287,0,true),
	(126,'http://dbpedia.org/ontology/Holiday','Holiday',0,0,true),
	(127,'http://dbpedia.org/ontology/Insect','Insect',300,0,true),
	(128,'http://dbpedia.org/ontology/Mineral','Mineral',327,0,true),
	(129,'http://dbpedia.org/ontology/MusicalWork','MusicalWork',198,0,false),
	(130,'http://dbpedia.org/ontology/SoccerClubSeason','SoccerClubSeason',324,0,true),
	(131,'http://dbpedia.org/ontology/MeanOfTransportation','MeanOfTransportation',0,0,false),
	(132,'http://dbpedia.org/ontology/NationalCollegiateAthleticAssociationAthlete','NationalCollegiateAthleticAssociationAthlete',157,0,true),
	(133,'http://dbpedia.org/ontology/Gene','Gene',315,0,false),
	(134,'http://dbpedia.org/ontology/Referee','Referee',226,0,true),
	(135,'http://dbpedia.org/ontology/Reptile','Reptile',300,0,true),
	(136,'http://dbpedia.org/ontology/CanadianFootballPlayer','CanadianFootballPlayer',50,0,true),
	(137,'http://dbpedia.org/ontology/GovernmentAgency','GovernmentAgency',318,0,true),
	(138,'http://dbpedia.org/ontology/Flag','Flag',0,0,true),
	(139,'http://dbpedia.org/ontology/Bacteria','Bacteria',333,0,true),
	(140,'http://dbpedia.org/ontology/Cardinal','Cardinal',114,0,true),
	(141,'http://dbpedia.org/ontology/Mollusca','Mollusca',300,0,true),
	(142,'http://dbpedia.org/ontology/Stadium','Stadium',358,0,true),
	(143,'http://dbpedia.org/ontology/Wine','Wine',21,0,false),
	(144,'http://dbpedia.org/ontology/NationalSoccerClub','NationalSoccerClub',339,0,true),
	(145,'http://dbpedia.org/ontology/Museum','Museum',358,0,true),
	(146,'http://dbpedia.org/ontology/FigureSkater','FigureSkater',157,0,true),
	(147,'http://dbpedia.org/ontology/Manga','Manga',205,0,true),
	(148,'http://dbpedia.org/ontology/College','College',275,0,true),
	(149,'http://dbpedia.org/ontology/NascarDriver','NascarDriver',157,0,true),
	(150,'http://dbpedia.org/ontology/PeriodicalLiterature','PeriodicalLiterature',159,0,false),
	(151,'http://dbpedia.org/ontology/Vein','Vein',8,0,true),
	(152,'http://dbpedia.org/ontology/Plant','Plant',272,0,false),
	(153,'http://dbpedia.org/ontology/Film','Film',198,0,true),
	(154,'http://dbpedia.org/ontology/SkiArea','SkiArea',320,0,true),(155,'http://dbpedia.org/ontology/Swimmer','Swimmer',157,0,true),(156,'http://dbpedia.org/ontology/PrimeMinister','PrimeMinister',100,0,true),(157,'http://dbpedia.org/ontology/Athlete','Athlete',226,0,false),(158,'http://dbpedia.org/ontology/Colour','Colour',0,970,true),(159,'http://dbpedia.org/ontology/WrittenWork','WrittenWork',198,0,false),(160,'http://dbpedia.org/ontology/SnookerChamp','SnookerChamp',118,0,true),(161,'http://dbpedia.org/ontology/Ambassador','Ambassador',226,0,true),(162,'http://dbpedia.org/ontology/SnookerWorldRanking','SnookerWorldRanking',0,0,true),(163,'http://dbpedia.org/ontology/RadioProgram','RadioProgram',198,0,true),(164,'http://dbpedia.org/ontology/Royalty','Royalty',226,0,false),(165,'http://dbpedia.org/ontology/BasketballTeam','BasketballTeam',287,0,true),(166,'http://dbpedia.org/ontology/Planet','Planet',245,0,true),(167,'http://dbpedia.org/ontology/Deputy','Deputy',100,0,true),(168,'http://dbpedia.org/ontology/YearInSpaceflight','YearInSpaceflight',64,0,true),(169,'http://dbpedia.org/ontology/Village','Village',57,0,true),(170,'http://dbpedia.org/ontology/Theatre','Theatre',358,0,true),(171,'http://dbpedia.org/ontology/ProtectedArea','ProtectedArea',320,0,true),(172,'http://dbpedia.org/ontology/Canal','Canal',85,0,true),(173,'http://dbpedia.org/ontology/MusicGenre','MusicGenre',326,0,true),(174,'http://dbpedia.org/ontology/Year','Year',64,0,true),(175,'http://dbpedia.org/ontology/Priest','Priest',114,0,true),(176,'http://dbpedia.org/ontology/Congressman','Congressman',100,0,true),(177,'http://dbpedia.org/ontology/Sport','Sport',184,0,true),(178,'http://dbpedia.org/ontology/Nerve','Nerve',8,0,true),(179,'http://dbpedia.org/ontology/SambaSchool','SambaSchool',318,0,true),(180,'http://dbpedia.org/ontology/Hotel','Hotel',358,0,true),(181,'http://dbpedia.org/ontology/Library','Library',275,0,true),(182,'http://dbpedia.org/ontology/Model','Model',226,0,true),(183,'http://dbpedia.org/ontology/Ligament','Ligament',8,0,true),(184,'http://dbpedia.org/ontology/Activity','Activity',0,1349,false),(185,'http://dbpedia.org/ontology/RecordLabel','RecordLabel',44,0,true),(186,'http://dbpedia.org/ontology/VoiceActor','VoiceActor',214,0,true),(187,'http://dbpedia.org/ontology/Olympics','Olympics',282,0,true),(188,'http://dbpedia.org/ontology/CanadianFootballLeague','CanadianFootballLeague',220,0,true),(189,'http://dbpedia.org/ontology/Food','Food',0,0,false),(190,'http://dbpedia.org/ontology/Song','Song',129,0,false),(191,'http://dbpedia.org/ontology/Play','Play',159,0,true),(192,'http://dbpedia.org/ontology/Album','Album',129,0,true),(193,'http://dbpedia.org/ontology/Sculpture','Sculpture',111,0,true),(194,'http://dbpedia.org/ontology/Airline','Airline',44,0,true),(195,'http://dbpedia.org/ontology/VolleyballLeague','VolleyballLeague',220,0,true),(196,'http://dbpedia.org/ontology/GrandPrix','GrandPrix',282,0,true),(197,'http://dbpedia.org/ontology/MountainPass','MountainPass',320,0,true),(198,'http://dbpedia.org/ontology/Work','Work',0,317544,false),(199,'http://dbpedia.org/ontology/Rocket','Rocket',131,0,true),(200,'http://dbpedia.org/ontology/Bird','Bird',300,0,true),(201,'http://dbpedia.org/ontology/GolfPlayer','GolfPlayer',157,0,true),(202,'http://dbpedia.org/ontology/BowlingLeague','BowlingLeague',220,0,true),(203,'http://dbpedia.org/ontology/Saint','Saint',114,0,true),(204,'http://dbpedia.org/ontology/HistoricalPeriod','HistoricalPeriod',4,0,true),(205,'http://dbpedia.org/ontology/Comics','Comics',159,0,false),(206,'http://dbpedia.org/ontology/PokerPlayer','PokerPlayer',157,0,true),(207,'http://dbpedia.org/ontology/Anime','Anime',364,0,true),(208,'http://dbpedia.org/ontology/Book','Book',159,0,false),(209,'http://dbpedia.org/ontology/Language','Language',0,0,true),(210,'http://dbpedia.org/ontology/Restaurant','Restaurant',358,0,true),(211,'http://dbpedia.org/ontology/Game','Game',184,0,true),(212,'http://dbpedia.org/ontology/Legislature','Legislature',318,0,true),(213,'http://dbpedia.org/ontology/MovieGenre','MovieGenre',326,0,true),(214,'http://dbpedia.org/ontology/Actor','Actor',266,0,false),(215,'http://dbpedia.org/ontology/Amphibian','Amphibian',300,0,true),(216,'http://dbpedia.org/ontology/GolfLeague','GolfLeague',220,0,true),(217,'http://dbpedia.org/ontology/Skyscraper','Skyscraper',358,0,true),(218,'http://dbpedia.org/ontology/Group','Group',318,0,true),(219,'http://dbpedia.org/ontology/Drug','Drug',0,0,true),(220,'http://dbpedia.org/ontology/SportsLeague','SportsLeague',318,0,false),(221,'http://dbpedia.org/ontology/BadmintonPlayer','BadmintonPlayer',157,0,true),(222,'http://dbpedia.org/ontology/Currency','Currency',0,242,true),(223,'http://dbpedia.org/ontology/ProgrammingLanguage','ProgrammingLanguage',0,0,true),(224,'http://dbpedia.org/ontology/ChessPlayer','ChessPlayer',157,0,true),(225,'http://dbpedia.org/ontology/RugbyPlayer','RugbyPlayer',157,0,true),(226,'http://dbpedia.org/ontology/Person','Person',255,0,false),(227,'http://dbpedia.org/ontology/Architect','Architect',226,0,true),(228,'http://dbpedia.org/ontology/HandballLeague','HandballLeague',220,0,true),(229,'http://dbpedia.org/ontology/Infrastructure','Infrastructure',62,0,false),(230,'http://dbpedia.org/ontology/Atoll','Atoll',336,0,true),(231,'http://dbpedia.org/ontology/SpaceMission','SpaceMission',64,0,true),(232,'http://dbpedia.org/ontology/RailwayLine','RailwayLine',323,0,true),(233,'http://dbpedia.org/ontology/CyclingCompetition','CyclingCompetition',282,0,true),(234,'http://dbpedia.org/ontology/RailwayTunnel','RailwayTunnel',90,0,true),(235,'http://dbpedia.org/ontology/Senator','Senator',100,0,true),(236,'http://dbpedia.org/ontology/ChemicalElement','ChemicalElement',327,0,true),(237,'http://dbpedia.org/ontology/GivenName','GivenName',39,0,true),(238,'http://dbpedia.org/ontology/Artery','Artery',8,347,true),(239,'http://dbpedia.org/ontology/FieldHockeyLeague','FieldHockeyLeague',220,0,true),(240,'http://dbpedia.org/ontology/TennisTournament','TennisTournament',282,0,true),(241,'http://dbpedia.org/ontology/Project','Project',0,0,false),(242,'http://dbpedia.org/ontology/Judge','Judge',226,0,true),(243,'http://dbpedia.org/ontology/Ship','Ship',131,0,true),(244,'http://dbpedia.org/ontology/Award','Award',0,1464,false),(245,'http://dbpedia.org/ontology/CelestialBody','CelestialBody',0,0,false),(246,'http://dbpedia.org/ontology/MartialArtist','MartialArtist',157,0,true),(247,'http://dbpedia.org/ontology/Musical','Musical',198,0,true),(248,'http://dbpedia.org/ontology/InlineHockeyLeague','InlineHockeyLeague',220,0,true),(249,'http://dbpedia.org/ontology/EurovisionSongContestEntry','EurovisionSongContestEntry',190,0,true),(250,'http://dbpedia.org/ontology/Bone','Bone',8,0,true),(251,'http://dbpedia.org/ontology/City','City',57,0,true),(252,'http://dbpedia.org/ontology/Convention','Convention',64,0,true),(253,'http://dbpedia.org/ontology/ShoppingMall','ShoppingMall',358,0,true),(254,'http://dbpedia.org/ontology/Journalist','Journalist',226,0,true),(255,'http://dbpedia.org/ontology/Agent','Agent',0,485419,false),(256,'http://dbpedia.org/ontology/Painting','Painting',111,0,true),(257,'http://dbpedia.org/ontology/OlympicResult','OlympicResult',0,0,true),(258,'http://dbpedia.org/ontology/SportsTeamMember','SportsTeamMember',72,0,true),(259,'http://dbpedia.org/ontology/MilitaryUnit','MilitaryUnit',318,0,true),(260,'http://dbpedia.org/ontology/SoccerTournament','SoccerTournament',282,0,true),(261,'http://dbpedia.org/ontology/Disease','Disease',0,0,true),(262,'http://dbpedia.org/ontology/Grape','Grape',28,0,true),(263,'http://dbpedia.org/ontology/BodyOfWater','BodyOfWater',81,0,false),(264,'http://dbpedia.org/ontology/HistoricBuilding','HistoricBuilding',358,0,true),(265,'http://dbpedia.org/ontology/Monument','Monument',320,0,true),(266,'http://dbpedia.org/ontology/Artist','Artist',226,0,false),(267,'http://dbpedia.org/ontology/GeopoliticalOrganisation','GeopoliticalOrganisation',318,0,true),(268,'http://dbpedia.org/ontology/MouseGene','MouseGene',133,0,true),(269,'http://dbpedia.org/ontology/AmericanFootballTeam','AmericanFootballTeam',287,0,true),(270,'http://dbpedia.org/ontology/GaelicGamesPlayer','GaelicGamesPlayer',157,0,true),(271,'http://dbpedia.org/ontology/Novel','Novel',208,0,false),(272,'http://dbpedia.org/ontology/Eukaryote','Eukaryote',333,0,false),(273,'http://dbpedia.org/ontology/VicePresident','VicePresident',100,0,true),(274,'http://dbpedia.org/ontology/BaseballLeague','BaseballLeague',220,0,true),(275,'http://dbpedia.org/ontology/EducationalInstitution','EducationalInstitution',318,0,false),(276,'http://dbpedia.org/ontology/SpaceStation','SpaceStation',131,0,true),(277,'http://dbpedia.org/ontology/Constellation','Constellation',0,0,true),(278,'http://dbpedia.org/ontology/PaintballLeague','PaintballLeague',220,0,true),(279,'http://dbpedia.org/ontology/ClubMoss','ClubMoss',152,0,true),(280,'http://dbpedia.org/ontology/LacrosseLeague','LacrosseLeague',220,0,true),(281,'http://dbpedia.org/ontology/FormulaOneRacing','FormulaOneRacing',220,0,true),(282,'http://dbpedia.org/ontology/SportsEvent','SportsEvent',64,0,false),(283,'http://dbpedia.org/ontology/Pope','Pope',114,0,true),(284,'http://dbpedia.org/ontology/ChemicalCompound','ChemicalCompound',327,0,true),(285,'http://dbpedia.org/ontology/WorldHeritageSite','WorldHeritageSite',320,0,true),(286,'http://dbpedia.org/ontology/AmericanFootballLeague','AmericanFootballLeague',220,0,true),(287,'http://dbpedia.org/ontology/SportsTeam','SportsTeam',318,0,false),(288,'http://dbpedia.org/ontology/AustralianFootballLeague','AustralianFootballLeague',220,0,true),(289,'http://dbpedia.org/ontology/BroadcastNetwork','BroadcastNetwork',379,0,true),(290,'http://dbpedia.org/ontology/HockeyTeam','HockeyTeam',287,0,true),(291,'http://dbpedia.org/ontology/Decoration','Decoration',244,0,true),(292,'http://dbpedia.org/ontology/WrestlingEvent','WrestlingEvent',282,0,true),(293,'http://dbpedia.org/ontology/Bridge','Bridge',323,0,true),(294,'http://dbpedia.org/ontology/Mountain','Mountain',81,0,true),(295,'http://dbpedia.org/ontology/Cave','Cave',81,0,true),(296,'http://dbpedia.org/ontology/Scientist','Scientist',226,0,true),(297,'http://dbpedia.org/ontology/Website','Website',198,0,true),(298,'http://dbpedia.org/ontology/Lymph','Lymph',8,0,true),(299,'http://dbpedia.org/ontology/ResearchProject','ResearchProject',241,0,true),(300,'http://dbpedia.org/ontology/Animal','Animal',272,0,false),(301,'http://dbpedia.org/ontology/Continent','Continent',336,0,true),(302,'http://dbpedia.org/ontology/Arena','Arena',358,0,true),(303,'http://dbpedia.org/ontology/SoccerLeagueSeason','SoccerLeagueSeason',51,0,true),(304,'http://dbpedia.org/ontology/Criminal','Criminal',226,0,true),(305,'http://dbpedia.org/ontology/Surname','Surname',39,0,true),(306,'http://dbpedia.org/ontology/Mammal','Mammal',300,0,true),(307,'http://dbpedia.org/ontology/Election','Election',64,0,true),(308,'http://dbpedia.org/ontology/BoxingLeague','BoxingLeague',220,0,true),(309,'http://dbpedia.org/ontology/RadioControlledRacingLeague','RadioControlledRacingLeague',220,0,true),(310,'http://dbpedia.org/ontology/Fungus','Fungus',272,0,true),(311,'http://dbpedia.org/ontology/Aircraft','Aircraft',131,0,true),(312,'http://dbpedia.org/ontology/FictionalCharacter','FictionalCharacter',226,0,false),(313,'http://dbpedia.org/ontology/Moss','Moss',152,0,true),(314,'http://dbpedia.org/ontology/TennisLeague','TennisLeague',220,0,true),(315,'http://dbpedia.org/ontology/Biomolecule','Biomolecule',0,9593,false),(316,'http://dbpedia.org/ontology/Station','Station',229,0,false),(317,'http://dbpedia.org/ontology/River','River',85,0,true),(318,'http://dbpedia.org/ontology/Organisation','Organisation',255,175252,false),(319,'http://dbpedia.org/ontology/ComicsCharacter','ComicsCharacter',312,0,true),(320,'http://dbpedia.org/ontology/Place','Place',0,0,false),(321,'http://dbpedia.org/ontology/Embryology','Embryology',8,0,true),(322,'http://dbpedia.org/ontology/Unknown','Unknown',0,0,true),(323,'http://dbpedia.org/ontology/RouteOfTransportation','RouteOfTransportation',229,0,false),(324,'http://dbpedia.org/ontology/SportsTeamSeason','SportsTeamSeason',318,0,false),(325,'http://dbpedia.org/ontology/GreenAlga','GreenAlga',152,0,true),(326,'http://dbpedia.org/ontology/Genre','Genre',0,0,false),(327,'http://dbpedia.org/ontology/ChemicalSubstance','ChemicalSubstance',0,6057,false),(328,'http://dbpedia.org/ontology/BaseballPlayer','BaseballPlayer',157,0,true),(329,'http://dbpedia.org/ontology/RoadTunnel','RoadTunnel',90,0,true),(330,'http://dbpedia.org/ontology/VicePrimeMinister','VicePrimeMinister',100,0,true),(331,'http://dbpedia.org/ontology/Instrument','Instrument',0,0,true),(332,'http://dbpedia.org/ontology/Mayor','Mayor',100,0,true),(333,'http://dbpedia.org/ontology/Species','Species',0,0,false),(334,'http://dbpedia.org/ontology/TelevisionStation','TelevisionStation',379,0,true),(335,'http://dbpedia.org/ontology/Race','Race',282,0,true),(336,'http://dbpedia.org/ontology/PopulatedPlace','PopulatedPlace',320,0,false),(337,'http://dbpedia.org/ontology/Cyclist','Cyclist',157,0,true),(338,'http://dbpedia.org/ontology/RugbyLeague','RugbyLeague',220,0,true),(339,'http://dbpedia.org/ontology/SoccerClub','SoccerClub',287,0,false),(340,'http://dbpedia.org/ontology/Asteroid','Asteroid',245,0,true),(341,'http://dbpedia.org/ontology/Weapon','Weapon',106,0,true),(342,'http://dbpedia.org/ontology/Lake','Lake',263,0,true),(343,'http://dbpedia.org/ontology/Lieutenant','Lieutenant',100,0,true),(344,'http://dbpedia.org/ontology/Celebrity','Celebrity',226,0,true),(345,'http://dbpedia.org/ontology/Letter','Letter',0,0,true),(346,'http://dbpedia.org/ontology/TopicalConcept','TopicalConcept',0,0,true),(347,'http://dbpedia.org/ontology/Lighthouse','Lighthouse',358,0,true),(348,'http://dbpedia.org/ontology/MemberOfParliament','MemberOfParliament',100,0,true),(349,'http://dbpedia.org/ontology/WineRegion','WineRegion',320,0,true),(350,'http://dbpedia.org/ontology/RailwayStation','RailwayStation',316,0,true),(351,'http://dbpedia.org/ontology/ChristianBishop','ChristianBishop',114,0,true),(352,'http://dbpedia.org/ontology/PoloLeague','PoloLeague',220,0,true),(353,'http://dbpedia.org/ontology/AutoRacingLeague','AutoRacingLeague',220,0,true),(354,'http://dbpedia.org/ontology/HistoricPlace','HistoricPlace',320,0,true),(355,'http://dbpedia.org/ontology/BritishRoyalty','BritishRoyalty',164,0,true),(356,'http://dbpedia.org/ontology/Manhua','Manhua',205,0,true),(357,'http://dbpedia.org/ontology/PolishKing','PolishKing',164,0,true),(358,'http://dbpedia.org/ontology/Building','Building',62,0,false),(359,'http://dbpedia.org/ontology/Astronaut','Astronaut',226,0,true),(360,'http://dbpedia.org/ontology/MixedMartialArtsLeague','MixedMartialArtsLeague',220,0,true),(361,'http://dbpedia.org/ontology/AdministrativeRegion','AdministrativeRegion',336,0,true),(362,'http://dbpedia.org/ontology/Single','Single',129,0,true),(363,'http://dbpedia.org/ontology/SoftballLeague','SoftballLeague',220,0,true),(364,'http://dbpedia.org/ontology/Cartoon','Cartoon',198,0,false),(365,'http://dbpedia.org/ontology/Spacecraft','Spacecraft',131,0,true),(366,'http://dbpedia.org/ontology/MixedMartialArtsEvent','MixedMartialArtsEvent',282,0,true),(367,'http://dbpedia.org/ontology/VideoGame','VideoGame',58,0,true),(368,'http://dbpedia.org/ontology/Governor','Governor',100,0,true),(369,'http://dbpedia.org/ontology/TradeUnion','TradeUnion',318,0,true),(370,'http://dbpedia.org/ontology/OfficeHolder','OfficeHolder',226,0,true),(371,'http://dbpedia.org/ontology/CricketLeague','CricketLeague',220,0,true),(372,'http://dbpedia.org/ontology/RugbyClub','RugbyClub',287,0,true),(373,'http://dbpedia.org/ontology/MountainRange','MountainRange',81,0,true),(374,'http://dbpedia.org/ontology/AustralianRulesFootballPlayer','AustralianRulesFootballPlayer',157,0,true),(375,'http://dbpedia.org/ontology/BasketballPlayer','BasketballPlayer',157,0,true),(376,'http://dbpedia.org/ontology/Chancellor','Chancellor',100,0,true),(377,'http://dbpedia.org/ontology/LegalCase','LegalCase',0,0,false),(378,'http://dbpedia.org/ontology/Crustacean','Crustacean',300,0,true),(379,'http://dbpedia.org/ontology/Broadcaster','Broadcaster',318,0,false),(380,'http://dbpedia.org/ontology/Cycad','Cycad',152,0,true);

DROP TABLE IF EXISTS errors;
CREATE TABLE errors (
  id serial NOT NULL,
  error_title character varying(255),
  example_uri character varying(255),
  example_n3 character varying(255),
  description character varying(10000),
  error_parent numeric(19,2),
  CONSTRAINT errors_pkey PRIMARY KEY (id)
);

INSERT INTO errors VALUES
	(3,'Object value is incorrectly extracted','http://dbpedia.org/resource/Oregon_Route_238','dbpprop:map \"238.0\"^^<http://dbpedia.org/datatype/second> .','The resource is about a \"Oregon Route 238\", which is a state highway and the property map has the value 238 which is incorrect. This is because Wikipedia has an attribute map, which has the image name as the value: map=Oregon Route 238.svg. The dbprop only extracted the value 238 from his attribute value and gave it a dataype second. ',2),
	(4,'Object value is incompletely extracted','http://dbpedia.org/resource/Dave_Dobbyn','dbpprop:dateOfBirth \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .','In this example, only the day of birth of a person is extracted and mapped to the dateofBirth property when it should have been the entire date ie. day,month and year. ',2),
	(5,'Special template not properly recognized','http://dbpedia.org/page/328_Gudrun','dbpprop:auto \"yes\"@en .','Many Wikipedia articles have an indication on top of the page, which says: \"This article does not cite any references or sources.\" referring to the fact that it is unreferenced. This information is stored in an attribute \"Unreferenced stub|auto=yes\". DBpedia thus extracts this attribute auto and value yes as a triple, which is not meaningful by itself and should not be extracted. ',2),
	(7,'Datatype incorrectly extracted','http://dbpedia.org/resource/Torishima_%28Izu_Islands%29','foaf:name, 鳥島@en','The datatype of a literal is incorrectly mapped. In this case, the name is not in English but has the datatype @en. ',6),
	(9,'One fact is encoded in several attributes ','http://dbpedia.org/resource/Barlinek','dbpprop:postalCodeType Postal code','In several cases, one fact in Wikipedia is encoded in several attributes. In this example, the value of the postal code of the town of Barlinek is encoded in two attributes \"postal_code_type = Postal code\" and \"postal_code = 74-320\". DBpedia extracts both of these attributes as is when it should have combined the attributes together to produce only the triple \"http://dbpedia.org/resource/Barlinek dbpprop:postalCode 74\" and not extract the postal code type as \"postal code\". ',8),
	(10,'Several facts are encoded in one attribute','http://dbpedia.org/resource/Picathartes','dbpedia-owl:synonym \"Galgulus Wagler, 1827 (non Brisson, 1760: preoccupied)\"@en . ','In this example, the triple is not incorrect but in fact contains two pieces of information in it. Only the first word is the synonym, the rest is a citation or reference. In Wikipedia, it is represented as \"synonyms = ''''Galgulus'''' <small>Wagler, 1827 (''''non'''' [[Mathurin Jacques Brisson|Brisson]], 1760: [[Coracias|preoccupied]])</small><br />\". Therefore, since several facts (synonym and reference, in this case) are encoded in one attribute, DBpedia should recognize and separate these facts into several triples. ',8),
	(14,'Extraction of attributes containing layout information','http://dbpedia.org/resource/L%C3%A6rdals%C3%B8yri','dbpprop:pushpinLabelPosition \"bottom\"@en .','Information related to layout of a page in Wikipedia, such as position of an image caption, position or size of an image, position of the co-ordinates are irrelevant when extracted in DBpedia. ',13),
	(15,'Redundant attributes value','http://dbpedia.org/resource/Niedersimmental_District','\"dbpedia-owl:thumbnail, foaf:depiction, dbpprop:imageMap Karte Bezirk Niedersimmental 2007.png\"\"\"','Three properties: dbpedia-owl:thumbnail, foaf:depiction, and dbpprop:imageMapRedundant refer to the same subject \"Karte_Bezirk_Niedersimmental_2007.png\" and are thus redundant in DBpedia. ',13),
	(16,'Image related information','http://dbpedia.org/resource/Three-banded_Plover','dbpprop:imageCaption At Masai Mara National Reserve, Kenya','Extraction of an image caption and name of the image are irrelevant when extracted in DBpedia. ',13),
	(17,'Other irrelevant information ','http://dbpedia.org/resource/Chestnut-banded_Plover','dbpprop:downloaded 2007-07-24 (xsd:date)','The date when the article is retrieved is extracted as a triple. This information is not present in Wikipedia''s Infobox but it refers to the date on which a link in the references was retrieved and therefore should not be extracted for this resource. ',13),
	(19,'Representation of number values','http://dbpedia.org/resource/Drei_Fl%C3%BCsse_Stadion','dbpprop:seatingCapacity \"20\"^^<http://www.w3.org/2001/XMLSchema#int> .','In Wkipedia, the seating capacity has the value \"20.000\", but the extractor is stopped by the period and does not include \"000\". ',18),
	(20,'Single double-quote inside literal value','http://dbpedia.org/resource/Arthur_Jensen','dbpprop:workInstitution \"Editorial boards of Intelligence and ''''Personality and Individual Differences\"@en .','in Wkipedia: work_institution = Editorial boards of ''''[[Intelligence (journal)|Intelligence]]'''' and ''''[[Personality and Individual Differences]] , which has the error of not completing the double quotes. Thus, instead of extracting two triples, it extracts the entire value as one. ',18),
	(21,'External websites (URLs)','http://dbpedia.org/page/Canaan_Valley','foaf:homepage <http://www.nature.nps.gov/nnl/Registry/USA_Map/States/West%20Virginia/NNL/CV/index.cfm> .','Links to external websites or external data sources are either incorrect or they do not show any information or are expired. ',27),
	(23,'Links to wikimedia','http://dbpedia.org/page/Wizard_of_New_Zealand','foaf:depiction <http://upload.wikimedia.org/wikipedia/commons/c/c4/Christchurchwizard.jpg> .','Links to wikimedia',22),
	(24,'Links to Freebase','http://dbpedia.org/page/ChaalBaaz','dbpprop:hasPhotoCollection http://www4.wiwiss.fu-berlin.de/flickrwrappr/photos/ChaalBaaz','Links to Freebase',22),
	(25,'Links to geospecies ','http://dbpedia.org/resource/Balkan_Mole','owl:sameAs <http://lod.geospecies.org/ses/LfBGE>.','Links to geospecies ',22),
	(26,'Links generated via flickr wrapper','http://dbpedia.org/page/ChaalBaaz','dbpprop:hasPhotoCollection http://www4.wiwiss.fu-berlin.de/flickrwrappr/photos/ChaalBaaz','Links generated via flickr wrapper',22);

DROP TABLE IF EXISTS evaluation_session;
CREATE TABLE evaluation_session (
  id SERIAL,
  campaign_id integer,
  user_id integer,
  -- "timestamp" timestamp without time zone,
    CONSTRAINT evaluation_session_pkey PRIMARY KEY (id),
    CONSTRAINT fke91f34937bce73b2 FOREIGN KEY (campaign_id)
        REFERENCES campaign (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fke91f3493f49ff1d2 FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS evaluated_resource;
CREATE TABLE evaluated_resource (
  id SERIAL,
  session_id integer,
  resource character varying(255),
  comments character varying(255),
  class character varying(255),
  correct boolean,
    CONSTRAINT evaluated_resource_pkey PRIMARY KEY (id),
    CONSTRAINT fk4f2da6e2fa763226 FOREIGN KEY (session_id)
        REFERENCES evaluation_session (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS evaluated_resource_details;
CREATE TABLE evaluated_resource_details (
  id SERIAL,
  resource_id integer,
  predicate character varying(255),
  object character varying(10000),
  error_id numeric(19,2),
  comment character varying(10000),
    CONSTRAINT evaluated_resource_details_pkey PRIMARY KEY (id),
    CONSTRAINT fkfffa3de524231a0d FOREIGN KEY (resource_id)
        REFERENCES evaluated_resource (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
