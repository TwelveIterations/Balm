- Revert parts of initial fix to fix another threading issue causing a crash on startup
  - Never doing workarounds for Sinytra Connector again

---

- Fixed threading issue causing Balm to not initialize things correctly when multiple Balm mods are present, causing lots of weird followup issues

---

- Fixed crash when mods falsely access Balm too early e.g. due to Sinytra Connector