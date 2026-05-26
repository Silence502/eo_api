-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Hôte : mysql-db
-- Généré le : mer. 08 avr. 2026 à 09:26
-- Version du serveur : 8.0.45
-- Version de PHP : 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

START TRANSACTION;

SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */
;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */
;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */
;
/*!40101 SET NAMES utf8mb4 */
;

--
-- Base de données : `eo_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `budget_categories`
--

CREATE TABLE `budget_categories` (
    `id` bigint UNSIGNED NOT NULL,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `budget_items`
--

CREATE TABLE `budget_items` (
    `id` bigint UNSIGNED NOT NULL,
    `event_id` bigint UNSIGNED NOT NULL,
    `currency_code` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'EUR',
    `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `estimated_amount` decimal(15, 2) DEFAULT '0.00',
    `actual_amount` decimal(15, 2) DEFAULT '0.00',
    `paid_amount` decimal(15, 2) DEFAULT '0.00',
    `due_date` date DEFAULT NULL,
    `status` enum(
        'pending',
        'partially_paid',
        'paid',
        'cancelled'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'pending',
    `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `companies`
--

CREATE TABLE `companies` (
    `id` bigint UNSIGNED NOT NULL,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `vat_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `currency_code` char(3) COLLATE utf8mb4_unicode_ci DEFAULT 'EUR',
    `street` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `other_street` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `country` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `state` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `zip_code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `notes` text COLLATE utf8mb4_unicode_ci,
    `logo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `company_user`
--

CREATE TABLE `company_user` (
    `company_id` bigint UNSIGNED NOT NULL,
    `user_id` bigint UNSIGNED NOT NULL,
    `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'member',
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `consents`
--

CREATE TABLE `consents` (
    `id` bigint UNSIGNED NOT NULL,
    `user_id` bigint UNSIGNED NOT NULL,
    `type` enum(
        'terms',
        'privacy',
        'marketing'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'terms',
    `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `given` tinyint(1) NOT NULL DEFAULT '0',
    `given_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `consents`
--

INSERT INTO
    `consents` (
        `id`,
        `user_id`,
        `type`,
        `version`,
        `given`,
        `given_at`,
        `ip_address`,
        `user_agent`,
        `created_at`
    )
VALUES (
        8,
        6,
        'terms',
        '1.0',
        1,
        '2026-04-06 14:23:54.000000',
        NULL,
        NULL,
        '2026-04-06 12:23:54.003996'
    ),
    (
        9,
        6,
        'privacy',
        '1.0',
        1,
        '2026-04-06 14:23:54.000000',
        NULL,
        NULL,
        '2026-04-06 12:23:54.006898'
    ),
    (
        10,
        6,
        'marketing',
        '1.0',
        0,
        '2026-04-06 14:23:54.000000',
        NULL,
        NULL,
        '2026-04-06 12:23:54.009661'
    );

-- --------------------------------------------------------

--
-- Structure de la table `events`
--

CREATE TABLE `events` (
    `id` bigint UNSIGNED NOT NULL,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `duration` int UNSIGNED DEFAULT NULL,
    `price` decimal(10, 2) NOT NULL DEFAULT '0.00',
    `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `location` bigint UNSIGNED DEFAULT NULL,
    `nb_guests` int UNSIGNED NOT NULL DEFAULT '0',
    `start_date` timestamp NULL DEFAULT NULL,
    `end_date` timestamp NULL DEFAULT NULL,
    `currency_code` char(3) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EUR',
    `duration_type` enum('minutes', 'hours', 'days') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'hours',
    `company_id` bigint UNSIGNED DEFAULT NULL,
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `event_manager`
--

CREATE TABLE `event_manager` (
    `event_id` bigint UNSIGNED NOT NULL,
    `user_id` bigint UNSIGNED NOT NULL,
    `permissions` set(
        'read',
        'write',
        'delete',
        'publish'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'read,write',
    `assigned_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `event_user`
--

CREATE TABLE `event_user` (
    `event_id` bigint UNSIGNED NOT NULL,
    `user_id` bigint UNSIGNED NOT NULL,
    `is_owner` tinyint(1) DEFAULT '0',
    `accepted_at` timestamp NULL DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `locations`
--

CREATE TABLE `locations` (
    `id` bigint UNSIGNED NOT NULL,
    `street` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `city` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `state` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `country` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `event_id` bigint UNSIGNED NOT NULL,
    `other_street` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `zip_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `settings`
--

CREATE TABLE `settings` (
    `user_id` bigint UNSIGNED NOT NULL,
    `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'en',
    `theme` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'light',
    `timezone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'Europe/Paris',
    `receive_notifications` tinyint(1) NOT NULL DEFAULT '1',
    `receive_reminders` tinyint(1) NOT NULL DEFAULT '1',
    `misc_01` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `misc_02` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `misc_03` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `id` bigint UNSIGNED NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `settings`
--

INSERT INTO
    `settings` (
        `user_id`,
        `language`,
        `theme`,
        `timezone`,
        `receive_notifications`,
        `receive_reminders`,
        `misc_01`,
        `misc_02`,
        `misc_03`,
        `id`
    )
VALUES (
        6,
        'en',
        'light',
        'Europe/Paris',
        1,
        1,
        NULL,
        NULL,
        NULL,
        4
    );

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
    `id` bigint UNSIGNED NOT NULL,
    `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `role` enum('user', 'admin', 'moderator') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
    `plan` enum('free', 'pro', 'enterprise') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'free',
    `first_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `last_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `hashed_refresh_token` text COLLATE utf8mb4_unicode_ci,
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO
    `users` (
        `id`,
        `email`,
        `password`,
        `role`,
        `plan`,
        `first_name`,
        `last_name`,
        `hashed_refresh_token`,
        `created_at`,
        `updated_at`
    )
VALUES (
        6,
        'user@example.com',
        '$2b$10$vG6Kcyw0Px7op2eHnl5LpO82bWxUPlw2iB1iOKpuqHoA8/i7QmQcS',
        'user',
        'free',
        'John',
        'Doe',
        '$2b$10$ZhVXXpwnsUzP2HAesHNhRuz9LOYxEI5S/wqh.YAz65p4HTzY/1psW',
        '2026-04-06 12:23:53.972584',
        '2026-04-06 12:26:24.000000'
    );

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `budget_items`
--
ALTER TABLE `budget_items`
ADD PRIMARY KEY (`id`),
ADD KEY `idx_event` (`event_id`);

--
-- Index pour la table `companies`
--
ALTER TABLE `companies` ADD PRIMARY KEY (`id`);

--
-- Index pour la table `company_user`
--
ALTER TABLE `company_user` ADD PRIMARY KEY (`company_id`, `user_id`);

--
-- Index pour la table `consents`
--
ALTER TABLE `consents` ADD PRIMARY KEY (`id`);

--
-- Index pour la table `events`
--
ALTER TABLE `events` ADD PRIMARY KEY (`id`);

--
-- Index pour la table `event_manager`
--
ALTER TABLE `event_manager`
ADD PRIMARY KEY (`event_id`, `user_id`),
ADD KEY `fk_em_user` (`user_id`);

--
-- Index pour la table `event_user`
--
ALTER TABLE `event_user`
ADD PRIMARY KEY (`event_id`, `user_id`),
ADD KEY `fk_eu_user` (`user_id`);

--
-- Index pour la table `locations`
--
ALTER TABLE `locations` ADD PRIMARY KEY (`id`);

--
-- Index pour la table `settings`
--
ALTER TABLE `settings` ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
ADD PRIMARY KEY (`id`),
ADD UNIQUE KEY `IDX_97672ac88f789774dd47f7c8be` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `budget_items`
--
ALTER TABLE `budget_items`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `companies`
--
ALTER TABLE `companies`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT pour la table `consents`
--
ALTER TABLE `consents`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 11;

--
-- AUTO_INCREMENT pour la table `events`
--
ALTER TABLE `events`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 9;

--
-- AUTO_INCREMENT pour la table `locations`
--
ALTER TABLE `locations`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT pour la table `settings`
--
ALTER TABLE `settings`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
AUTO_INCREMENT = 7;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `budget_items`
--
ALTER TABLE `budget_items`
ADD CONSTRAINT `fk_event_budget` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `event_manager`
--
ALTER TABLE `event_manager`
ADD CONSTRAINT `fk_em_event` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_em_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `event_user`
--
ALTER TABLE `event_user`
ADD CONSTRAINT `fk_eu_event` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_eu_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */
;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */
;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */
;