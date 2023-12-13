package com.poledisplayapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdApprovalModel {
    @JsonProperty("ad_id")
    private String adId;

    @JsonProperty("ad")
    private Ad ad;

    @JsonProperty("approved_networks")
    private List<String> approvedNetworks;

    @JsonProperty("audit")
    private Audit audit;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("creative")
    private Creative creative;

    @JsonProperty("flagged_reason")
    private String flaggedReason;

    @JsonProperty("flagged_custom_reason")
    private String flaggedCustomReason;

    @JsonProperty("id")
    private String id;

    @JsonProperty("is_approved_on_all_networks")
    private boolean isApprovedOnAllNetworks;

    @JsonProperty("name")
    private String name;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("owned_by")
    private String ownedBy;

    @JsonProperty("ts")
    private int ts;

    // Getter and setter methods...


    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public List<String> getApprovedNetworks() {
        return approvedNetworks;
    }

    public void setApprovedNetworks(List<String> approvedNetworks) {
        this.approvedNetworks = approvedNetworks;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Creative getCreative() {
        return creative;
    }

    public void setCreative(Creative creative) {
        this.creative = creative;
    }

    public String getFlaggedReason() {
        return flaggedReason;
    }

    public void setFlaggedReason(String flaggedReason) {
        this.flaggedReason = flaggedReason;
    }

    public String getFlaggedCustomReason() {
        return flaggedCustomReason;
    }

    public void setFlaggedCustomReason(String flaggedCustomReason) {
        this.flaggedCustomReason = flaggedCustomReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isApprovedOnAllNetworks() {
        return isApprovedOnAllNetworks;
    }

    public void setApprovedOnAllNetworks(boolean approvedOnAllNetworks) {
        isApprovedOnAllNetworks = approvedOnAllNetworks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public static class Ad {
        @JsonProperty("adomain")
        private List<String> adomain;

        @JsonProperty("bundle")
        private List<String> bundle;

        @JsonProperty("cat")
        private List<String> cat;

        @JsonProperty("created_by")
        private String createdBy;

        @JsonProperty("dadid")
        private String dadid;

        @JsonProperty("id")
        private String id;

        @JsonProperty("lang")
        private String lang;

        @JsonProperty("name")
        private String name;

        @JsonProperty("notes")
        private String notes;

        @JsonProperty("owned_by")
        private String ownedBy;

        @JsonProperty("sadid")
        private String sadid;

        @JsonProperty("secure")
        private Object secure;

        @JsonProperty("audit")
        private Audit audit;

        @JsonProperty("creative")
        private Creative creative;

        @JsonProperty("ts")
        private int ts;


        @JsonProperty("events")
        private List<Object> events;

        // Getter and setter methods...


        public List<String> getAdomain() {
            return adomain;
        }

        public void setAdomain(List<String> adomain) {
            this.adomain = adomain;
        }

        public List<String> getBundle() {
            return bundle;
        }

        public void setBundle(List<String> bundle) {
            this.bundle = bundle;
        }

        public List<String> getCat() {
            return cat;
        }

        public void setCat(List<String> cat) {
            this.cat = cat;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getDadid() {
            return dadid;
        }

        public void setDadid(String dadid) {
            this.dadid = dadid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getOwnedBy() {
            return ownedBy;
        }

        public void setOwnedBy(String ownedBy) {
            this.ownedBy = ownedBy;
        }

        public String getSadid() {
            return sadid;
        }

        public void setSadid(String sadid) {
            this.sadid = sadid;
        }

        public Object getSecure() {
            return secure;
        }

        public void setSecure(Object secure) {
            this.secure = secure;
        }

        public Audit getAudit() {
            return audit;
        }

        public void setAudit(Audit audit) {
            this.audit = audit;
        }

        public Creative getCreative() {
            return creative;
        }

        public void setCreative(Creative creative) {
            this.creative = creative;
        }

        public int getTs() {
            return ts;
        }

        public void setTs(int ts) {
            this.ts = ts;
        }


        public List<Object> getEvents() {
            return events;
        }

        public void setEvents(List<Object> events) {
            this.events = events;
        }
    }

    public static class Audit {
        @JsonProperty("feedback")
        private List<Object> feedback;

        @JsonProperty("lastmod")
        private String lastmod;

        @JsonProperty("status")
        private int status;

        @JsonProperty("corr")
        private Object corr;

        // Getter and setter methods...


        public List<Object> getFeedback() {
            return feedback;
        }

        public void setFeedback(List<Object> feedback) {
            this.feedback = feedback;
        }

        public String getLastmod() {
            return lastmod;
        }

        public void setLastmod(String lastmod) {
            this.lastmod = lastmod;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getCorr() {
            return corr;
        }

        public void setCorr(Object corr) {
            this.corr = corr;
        }
    }

    public static class Creative {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("h")
        private int h;

        @JsonProperty("w")
        private int w;

        @JsonProperty("status")
        private String status;

        @JsonProperty("type")
        private String type;

        @JsonProperty("markup")
        private String markup;

        @JsonProperty("snapshots")
        private List<Snapshot> snapshots;

        @JsonProperty("cta")
        private CTA cta;

        @JsonProperty("ext")
        private Ext ext;

        // Getter and setter methods...


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMarkup() {
            return markup;
        }

        public void setMarkup(String markup) {
            this.markup = markup;
        }

        public List<Snapshot> getSnapshots() {
            return snapshots;
        }

        public void setSnapshots(List<Snapshot> snapshots) {
            this.snapshots = snapshots;
        }

        public CTA getCta() {
            return cta;
        }

        public void setCta(CTA cta) {
            this.cta = cta;
        }

        public Ext getExt() {
            return ext;
        }

        public void setExt(Ext ext) {
            this.ext = ext;
        }

        public static class Snapshot {
            @JsonProperty("h")
            private int h;

            @JsonProperty("markup")
            private String markup;

            @JsonProperty("mime")
            private String mime;

            @JsonProperty("iurl")
            private String iurl;

            @JsonProperty("scaling_factor")
            private String scalingFactor;

            @JsonProperty("w")
            private int w;

            // Getter and setter methods...


            public int getH() {
                return h;
            }

            public void setH(int h) {
                this.h = h;
            }

            public String getMarkup() {
                return markup;
            }

            public void setMarkup(String markup) {
                this.markup = markup;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public String getIurl() {
                return iurl;
            }

            public void setIurl(String iurl) {
                this.iurl = iurl;
            }

            public String getScalingFactor() {
                return scalingFactor;
            }

            public void setScalingFactor(String scalingFactor) {
                this.scalingFactor = scalingFactor;
            }

            public int getW() {
                return w;
            }

            public void setW(int w) {
                this.w = w;
            }
        }

        public static class CTA {
            @JsonProperty("text")
            private String text;

            @JsonProperty("markup")
            private String markup;

            // Getter and setter methods...


            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getMarkup() {
                return markup;
            }

            public void setMarkup(String markup) {
                this.markup = markup;
            }
        }

        public static class Ext {
            @JsonProperty("snapshots")
            private List<SnapshotExt> snapshots;

            @JsonProperty("preferred_scale")
            private Object preferredScale;

            @JsonProperty("base_img_url")
            private String baseImgUrl;

            @JsonProperty("geographic_precision")
            private Object geographicPrecision;

            @JsonProperty("geographic_specificity")
            private Object geographicSpecificity;

            @JsonProperty("refresh_rate_in_seconds")
            private Object refreshRateInSeconds;

            // Getter and setter methods...


            public List<SnapshotExt> getSnapshots() {
                return snapshots;
            }

            public void setSnapshots(List<SnapshotExt> snapshots) {
                this.snapshots = snapshots;
            }

            public Object getPreferredScale() {
                return preferredScale;
            }

            public void setPreferredScale(Object preferredScale) {
                this.preferredScale = preferredScale;
            }

            public String getBaseImgUrl() {
                return baseImgUrl;
            }

            public void setBaseImgUrl(String baseImgUrl) {
                this.baseImgUrl = baseImgUrl;
            }

            public Object getGeographicPrecision() {
                return geographicPrecision;
            }

            public void setGeographicPrecision(Object geographicPrecision) {
                this.geographicPrecision = geographicPrecision;
            }

            public Object getGeographicSpecificity() {
                return geographicSpecificity;
            }

            public void setGeographicSpecificity(Object geographicSpecificity) {
                this.geographicSpecificity = geographicSpecificity;
            }

            public Object getRefreshRateInSeconds() {
                return refreshRateInSeconds;
            }

            public void setRefreshRateInSeconds(Object refreshRateInSeconds) {
                this.refreshRateInSeconds = refreshRateInSeconds;
            }

            public static class SnapshotExt {
                @JsonProperty("h")
                private int h;

                @JsonProperty("markup")
                private String markup;

                @JsonProperty("mime")
                private String mime;

                @JsonProperty("iurl")
                private String iurl;

                @JsonProperty("scaling_factor")
                private String scalingFactor;

                @JsonProperty("w")
                private int w;

                // Getter and setter methods...


                public int getH() {
                    return h;
                }

                public void setH(int h) {
                    this.h = h;
                }

                public String getMarkup() {
                    return markup;
                }

                public void setMarkup(String markup) {
                    this.markup = markup;
                }

                public String getMime() {
                    return mime;
                }

                public void setMime(String mime) {
                    this.mime = mime;
                }

                public String getIurl() {
                    return iurl;
                }

                public void setIurl(String iurl) {
                    this.iurl = iurl;
                }

                public String getScalingFactor() {
                    return scalingFactor;
                }

                public void setScalingFactor(String scalingFactor) {
                    this.scalingFactor = scalingFactor;
                }

                public int getW() {
                    return w;
                }

                public void setW(int w) {
                    this.w = w;
                }
            }
        }
    }
}
