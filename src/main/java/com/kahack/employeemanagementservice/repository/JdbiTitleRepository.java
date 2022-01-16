package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Title;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

@Builder
public class JdbiTitleRepository implements TitleRepository{
    private static String FIND_TITLE_USING_NAME = "SELECT BIN_TO_UUID(title_id) AS title_id, name FROM title WHERE name=:name";
    private final Jdbi jdbi;

    public String fetchTitleId(String titleName) {
        try {
            Optional<Title> title = findTitleRecord(titleName);

            if (title.isPresent()) {
                return title.get().getTitleId();
            }

            String titleId = generateUUID();
            addTitle(Title.builder().titleId(titleId).name(titleName).build());

            return titleId;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String generateUUID() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT UUID()").mapTo(String.class).first());
    }

    @Override
    public Optional<Title> findTitleRecord(String titleName) {
        return jdbi.withHandle(handle -> handle
                .createQuery(FIND_TITLE_USING_NAME)
                .bind("name", titleName)
                .mapToBean(Title.class)
                .findFirst());
    }

    @Override
    public void addTitle(Title title) {
        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO title(title_id, name) VALUES(UUID_TO_BIN(?),?)")
                .bind(0, title.getTitleId())
                .bind(1, title.getName())
                .execute());
    }
}
