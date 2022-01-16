package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Title;

import java.util.Optional;

public interface TitleRepository {
    String fetchTitleId(final String titleName);
    Optional<Title> findTitleRecord(final String titleName);
    void addTitle(final Title title);
}
