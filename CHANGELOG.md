# Changelog

All notable changes to JackSparrow will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added
- MySQL 数据源支持 (HikariCP 连接池)
- PostgreSQL 数据源支持 (HikariCP 连接池)
- 分布式锁服务 (基于 Redis)
- 连接池监控指标 (Micrometer + Prometheus)
- Grafana 可视化支持
- Docker Compose 完整配置 (MySQL/PG/Prometheus/Grafana)
- 数据库初始化脚本
- Prometheus 监控配置
- CONTRIBUTING.md 贡献指南
- EXAMPLES.md 使用示例文档

### Changed
- 升级 FastJSON 到 2.0.43 (安全修复)
- 重构 Redis 控制器为 RESTful 风格
- 更新 README.md 添加完整 API 表格

### Improved
- 异常处理体系完善
- 日志配置优化 (Logback)
- API 文档完善 (Swagger/OpenAPI)

---

## [1.0.0] - 2026-03-25

### Added
- 初始版本发布
- Redis 数据源支持
- Elasticsearch 数据源支持
- 多数据源管理框架
- Spring Boot 3.5.11 基础架构
- Swagger API 文档
- Docker 支持
- 单元测试框架

---

## Legend

- `Added` - 新增功能
- `Changed` - 变更
- `Deprecated` - 即将弃用
- `Removed` - 移除
- `Fixed` - 修复
- `Improved` - 改进
- `Security` - 安全相关
