/**
 * PetCareBook - Clinical Application Common JavaScript
 * High-Resilience Enterprise Data Client & UI Engine
 */

// Global SVG Icons (Zero Emojis, Clinical Enterprise Standard)
var ICONS = {
    trash: '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>',
    plus: '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>',
    alert: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#dc2626" stroke-width="2.5"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>',
    warning: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#d97706" stroke-width="2.5"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"></path></svg>',
    calendar: '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>',
    shield: '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>',
    check: '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#16a34a" stroke-width="2.5"><polyline points="20 6 9 17 4 12"></polyline></svg>'
};
window.ICONS = ICONS;
window.getIcon = function(name) { return ICONS[name] || ''; };

// If opened from file://, point to http://localhost:8080, otherwise use relative path
var API_BASE = window.location.protocol === 'file:' ? 'http://localhost:8080' : '';

// Built-in Offline Fallback Storage (Preserves complete context even if backend server is offline)
var FALLBACK_STORE = {
    owners: [
        { id: 1, fullName: "Sarah Connor", email: "sarah.connor@example.com", phoneNumber: "+1 (555) 234-5678", address: "742 Evergreen Terrace, Springfield", petCount: 2, createdAt: "2026-09-01T10:00:00" },
        { id: 2, fullName: "David Miller", email: "david.miller@example.com", phoneNumber: "+1 (555) 876-5432", address: "221B Baker Street, London", petCount: 1, createdAt: "2026-09-05T11:30:00" },
        { id: 3, fullName: "Emma Watson", email: "emma.watson@example.com", phoneNumber: "+1 (555) 432-1098", address: "4 Privet Drive, Little Whinging", petCount: 2, createdAt: "2026-09-10T14:15:00" }
    ],
    pets: [
        { id: 1, name: "Milo", species: "Dog", breed: "Golden Retriever", dateOfBirth: "2024-06-28", ageYears: 2, ageMonths: 3, gender: "Male", weight: 31.5, ownerId: 1, ownerName: "Sarah Connor", ownerEmail: "sarah.connor@example.com", ownerPhone: "+1 (555) 234-5678", vaccinationRecordCount: 2 },
        { id: 2, name: "Luna", species: "Cat", breed: "Siamese", dateOfBirth: "2025-03-28", ageYears: 1, ageMonths: 6, gender: "Female", weight: 4.2, ownerId: 2, ownerName: "David Miller", ownerEmail: "david.miller@example.com", ownerPhone: "+1 (555) 876-5432", vaccinationRecordCount: 1 },
        { id: 3, name: "Charlie", species: "Dog", breed: "Beagle", dateOfBirth: "2023-08-28", ageYears: 3, ageMonths: 1, gender: "Male", weight: 14.0, ownerId: 1, ownerName: "Sarah Connor", ownerEmail: "sarah.connor@example.com", ownerPhone: "+1 (555) 234-5678", vaccinationRecordCount: 1 },
        { id: 4, name: "Oliver", species: "Cat", breed: "Tabby", dateOfBirth: "2025-12-28", ageYears: 0, ageMonths: 9, gender: "Male", weight: 3.8, ownerId: 3, ownerName: "Emma Watson", ownerEmail: "emma.watson@example.com", ownerPhone: "+1 (555) 432-1098", vaccinationRecordCount: 1 },
        { id: 5, name: "Coco", species: "Dog", breed: "Poodle", dateOfBirth: "2025-07-28", ageYears: 1, ageMonths: 2, gender: "Female", weight: 7.4, ownerId: 3, ownerName: "Emma Watson", ownerEmail: "emma.watson@example.com", ownerPhone: "+1 (555) 432-1098", vaccinationRecordCount: 1 }
    ],
    vaccines: [
        { id: 1, name: "Rabies (Canine & Feline)", targetSpecies: "All", description: "Core annual vaccine preventing rabies virus transmission.", standardIntervalDays: 365, mandatory: true },
        { id: 2, name: "DHPP (Distemper, Parvo, Hepatitis)", targetSpecies: "Dog", description: "Core canine combination vaccine protecting against fatal viral infections.", standardIntervalDays: 365, mandatory: true },
        { id: 3, name: "Bordetella (Kennel Cough)", targetSpecies: "Dog", description: "Recommended bi-annual protection for dogs in boarding or day care.", standardIntervalDays: 180, mandatory: false },
        { id: 4, name: "Canine Broad-Spectrum Deworming", targetSpecies: "Dog", description: "Quarterly internal parasite and intestinal worm control protocol.", standardIntervalDays: 90, mandatory: false },
        { id: 5, name: "FVRCP (Feline Core)", targetSpecies: "Cat", description: "Core feline protection against Rhinotracheitis, Calicivirus, and Panleukopenia.", standardIntervalDays: 365, mandatory: true },
        { id: 6, name: "Feline Leukemia Virus (FeLV)", targetSpecies: "Cat", description: "Recommended for outdoor cats or multi-cat environments.", standardIntervalDays: 365, mandatory: false }
    ],
    vaccinations: [
        { id: 1, petId: 1, petName: "Milo", species: "Dog", breed: "Golden Retriever", petDateOfBirth: "2024-06-28", ownerId: 1, ownerName: "Sarah Connor", ownerEmail: "sarah.connor@example.com", ownerPhone: "+1 (555) 234-5678", vaccineTypeId: 2, vaccineName: "DHPP (Distemper, Parvo, Hepatitis)", standardIntervalDays: 365, dateAdministered: "2025-10-03", nextDueDate: "2026-10-03", daysRemainingUntilDue: 5, status: "DUE_SOON", administeredBy: "Green Valley Animal Hospital", batchNumber: "DHPP-2025-081", notes: "Annual booster scheduled. Pet in great health." },
        { id: 2, petId: 2, petName: "Luna", species: "Cat", breed: "Siamese", petDateOfBirth: "2025-03-28", ownerId: 2, ownerName: "David Miller", ownerEmail: "david.miller@example.com", ownerPhone: "+1 (555) 876-5432", vaccineTypeId: 5, vaccineName: "FVRCP (Feline Core)", standardIntervalDays: 365, dateAdministered: "2025-10-01", nextDueDate: "2026-10-01", daysRemainingUntilDue: 3, status: "DUE_SOON", administeredBy: "City Feline Wellness Clinic", batchNumber: "FVRCP-993-B", notes: "Healthy coat and clear eyes." },
        { id: 3, petId: 5, petName: "Coco", species: "Dog", breed: "Poodle", petDateOfBirth: "2025-07-28", ownerId: 3, ownerName: "Emma Watson", ownerEmail: "emma.watson@example.com", ownerPhone: "+1 (555) 432-1098", vaccineTypeId: 4, vaccineName: "Canine Broad-Spectrum Deworming", standardIntervalDays: 90, dateAdministered: "2026-07-05", nextDueDate: "2026-10-03", daysRemainingUntilDue: 5, status: "DUE_SOON", administeredBy: "Springfield Vet Care", batchNumber: "DWM-441-A", notes: "Routine preventive deworming administered." },
        { id: 4, petId: 3, petName: "Charlie", species: "Dog", breed: "Beagle", petDateOfBirth: "2023-08-28", ownerId: 1, ownerName: "Sarah Connor", ownerEmail: "sarah.connor@example.com", ownerPhone: "+1 (555) 234-5678", vaccineTypeId: 3, vaccineName: "Bordetella (Kennel Cough)", standardIntervalDays: 180, dateAdministered: "2026-03-22", nextDueDate: "2026-09-18", daysRemainingUntilDue: -10, status: "OVERDUE", administeredBy: "Happy Paws Veterinary", batchNumber: "BORD-1092", notes: "Boarding requirement; needs urgent renewal." },
        { id: 5, petId: 1, petName: "Milo", species: "Dog", breed: "Golden Retriever", petDateOfBirth: "2024-06-28", ownerId: 1, ownerName: "Sarah Connor", ownerEmail: "sarah.connor@example.com", ownerPhone: "+1 (555) 234-5678", vaccineTypeId: 1, vaccineName: "Rabies (Canine & Feline)", standardIntervalDays: 365, dateAdministered: "2026-07-30", nextDueDate: "2027-07-30", daysRemainingUntilDue: 305, status: "UP_TO_DATE", administeredBy: "Green Valley Animal Hospital", batchNumber: "RAB-2026-X1", notes: "Rabies tag #4492 issued." },
        { id: 6, petId: 4, petName: "Oliver", species: "Cat", breed: "Tabby", petDateOfBirth: "2025-12-28", ownerId: 3, ownerName: "Emma Watson", ownerEmail: "emma.watson@example.com", ownerPhone: "+1 (555) 432-1098", vaccineTypeId: 5, vaccineName: "FVRCP (Feline Core)", standardIntervalDays: 365, dateAdministered: "2026-08-29", nextDueDate: "2027-08-29", daysRemainingUntilDue: 335, status: "UP_TO_DATE", administeredBy: "Springfield Vet Care", batchNumber: "FVRCP-102-K", notes: "Kitten initial vaccination complete." }
    ]
};

var isBackendOffline = false;

function showBackendNoticeOnce() {
    if (document.getElementById('backendStatusBanner')) return;
    var banner = document.createElement('div');
    banner.id = 'backendStatusBanner';
    banner.style.cssText = 'background: #eff6ff; color: #1e40af; border-bottom: 1px solid #bfdbfe; padding: 0.5rem 1rem; font-size: 0.8rem; text-align: center; font-weight: 500; position: sticky; top: 0; z-index: 1000;';
    banner.innerHTML = '<strong>Standalone Clinical Mode:</strong> Displaying embedded patient data. To connect with the live Spring Boot JPA/H2 database on port 8080, run <code>.\\run.ps1</code> or <code>run.bat</code> in terminal.';
    document.body.insertBefore(banner, document.body.firstChild);
}

// Resilient API Client with Graceful Offline Fallback
async function apiFetch(endpoint, options) {
    options = options || {};
    var defaultHeaders = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };
    options.headers = Object.assign({}, defaultHeaders, options.headers || {});

    var response;
    try {
        response = await fetch(API_BASE + endpoint, options);
    } catch (netErr) {
        // Network unreachable or offline -> Fallback to embedded demo data
        console.warn('Backend server unreachable at ' + (API_BASE + endpoint) + '. Using fallback store.', netErr.message);
        isBackendOffline = true;
        showBackendNoticeOnce();
        return handleOfflineMock(endpoint, options);
    }

    if (response.status === 204) {
        return null;
    }

    var data;
    try {
        data = await response.json();
    } catch (jsonErr) {
        data = null;
    }

    if (!response.ok) {
        var errorMessage = (data && data.message) ? data.message : ('HTTP ' + response.status + ' ' + response.statusText);
        if (data && data.validationErrors) {
            var details = Object.entries(data.validationErrors)
                .map(function(e) { return e[0] + ': ' + e[1]; })
                .join('; ');
            errorMessage += ' (' + details + ')';
        }
        var err = new Error(errorMessage);
        err.status = response.status;
        throw err;
    }

    return data;
}

// Complete Offline Mock Store Engine
function handleOfflineMock(endpoint, options) {
    var method = (options.method || 'GET').toUpperCase();
    var cleanUrl = endpoint.split('?')[0];

    // 1. Dashboard Metrics
    if (cleanUrl === '/api/dashboard/stats') {
        var dueSoonList = FALLBACK_STORE.vaccinations.filter(function(v) { return v.daysRemainingUntilDue >= 0 && v.daysRemainingUntilDue <= 7; });
        var overdueList = FALLBACK_STORE.vaccinations.filter(function(v) { return v.daysRemainingUntilDue < 0; });
        return {
            totalPets: FALLBACK_STORE.pets.length,
            totalOwners: FALLBACK_STORE.owners.length,
            totalVaccineTypes: FALLBACK_STORE.vaccines.length,
            totalVaccinationsGiven: FALLBACK_STORE.vaccinations.length,
            dueInNext7DaysCount: dueSoonList.length,
            overdueCount: overdueList.length
        };
    }

    // 2. Pets API
    if (cleanUrl === '/api/pets') {
        if (method === 'POST') {
            var payload = JSON.parse(options.body);
            var owner = FALLBACK_STORE.owners.find(function(o) { return o.id === payload.ownerId; }) || { fullName: "Client", email: "", phoneNumber: "" };
            var newPet = Object.assign({}, payload, {
                id: Date.now(),
                ownerName: owner.fullName,
                ownerEmail: owner.email,
                ownerPhone: owner.phoneNumber,
                ageYears: 1,
                ageMonths: 0,
                vaccinationRecordCount: 0
            });
            FALLBACK_STORE.pets.unshift(newPet);
            var ownerObj = FALLBACK_STORE.owners.find(function(o) { return o.id === payload.ownerId; });
            if (ownerObj) ownerObj.petCount = (ownerObj.petCount || 0) + 1;
            return newPet;
        }

        var urlParams = new URLSearchParams(endpoint.indexOf('?') !== -1 ? endpoint.split('?')[1] : '');
        var speciesFilter = urlParams.get('species');
        var searchFilter = urlParams.get('search');
        var ownerIdFilter = urlParams.get('ownerId');

        var list = FALLBACK_STORE.pets.slice();
        if (ownerIdFilter) list = list.filter(function(p) { return p.ownerId == ownerIdFilter; });
        if (speciesFilter && speciesFilter !== 'all') list = list.filter(function(p) { return p.species.toLowerCase() === speciesFilter.toLowerCase(); });
        if (searchFilter) {
            var q = searchFilter.toLowerCase();
            list = list.filter(function(p) { return p.name.toLowerCase().includes(q) || p.breed.toLowerCase().includes(q); });
        }
        return list;
    }

    // Single Pet Details or History
    var petHistoryMatch = endpoint.match(/\/api\/pets\/(\d+)\/vaccinations/);
    if (petHistoryMatch) {
        var petId = parseInt(petHistoryMatch[1], 10);
        return FALLBACK_STORE.vaccinations.filter(function(v) { return v.petId === petId; });
    }

    var petMatch = endpoint.match(/\/api\/pets\/(\d+)/);
    if (petMatch && method === 'DELETE') {
        var delId = parseInt(petMatch[1], 10);
        FALLBACK_STORE.pets = FALLBACK_STORE.pets.filter(function(p) { return p.id !== delId; });
        FALLBACK_STORE.vaccinations = FALLBACK_STORE.vaccinations.filter(function(v) { return v.petId !== delId; });
        return null;
    }

    // 3. Owners API
    if (cleanUrl === '/api/owners') {
        if (method === 'POST') {
            var oPayload = JSON.parse(options.body);
            var newOwner = Object.assign({}, oPayload, { id: Date.now(), petCount: 0, createdAt: new Date().toISOString() });
            FALLBACK_STORE.owners.unshift(newOwner);
            return newOwner;
        }
        return FALLBACK_STORE.owners.slice();
    }

    var ownerMatch = endpoint.match(/\/api\/owners\/(\d+)/);
    if (ownerMatch && method === 'DELETE') {
        var oDelId = parseInt(ownerMatch[1], 10);
        FALLBACK_STORE.owners = FALLBACK_STORE.owners.filter(function(o) { return o.id !== oDelId; });
        FALLBACK_STORE.pets = FALLBACK_STORE.pets.filter(function(p) { return p.ownerId !== oDelId; });
        return null;
    }

    // 4. Vaccine Types API
    if (cleanUrl === '/api/vaccine-types') {
        if (method === 'POST') {
            var vPayload = JSON.parse(options.body);
            var newVt = Object.assign({}, vPayload, { id: Date.now() });
            FALLBACK_STORE.vaccines.push(newVt);
            return newVt;
        }
        var vParts = endpoint.split('?');
        if (vParts.length > 1) {
            var vParams = new URLSearchParams(vParts[1]);
            var sp = vParams.get('species');
            if (sp && sp !== 'all') {
                return FALLBACK_STORE.vaccines.filter(function(v) { return v.targetSpecies.toLowerCase() === sp.toLowerCase() || v.targetSpecies.toLowerCase() === 'all'; });
            }
        }
        return FALLBACK_STORE.vaccines.slice();
    }

    // 5. Vaccinations API
    if (cleanUrl === '/api/vaccinations') {
        if (method === 'POST') {
            var recPayload = JSON.parse(options.body);
            var targetPet = FALLBACK_STORE.pets.find(function(p) { return p.id === recPayload.petId; });
            var targetVaccine = FALLBACK_STORE.vaccines.find(function(v) { return v.id === recPayload.vaccineTypeId; });
            
            var adminDate = new Date(recPayload.dateAdministered);
            var interval = targetVaccine ? targetVaccine.standardIntervalDays : 365;
            var dueDate = new Date(adminDate);
            dueDate.setDate(dueDate.getDate() + interval);
            var dueStr = dueDate.toISOString().split('T')[0];

            var today = new Date();
            var diffTime = dueDate.getTime() - today.getTime();
            var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            var st = "UP_TO_DATE";
            if (diffDays < 0) st = "OVERDUE";
            else if (diffDays <= 7) st = "DUE_SOON";

            var newRecord = {
                id: Date.now(),
                petId: targetPet ? targetPet.id : recPayload.petId,
                petName: targetPet ? targetPet.name : "Patient",
                species: targetPet ? targetPet.species : "Dog",
                breed: targetPet ? targetPet.breed : "Mixed",
                ownerName: targetPet ? targetPet.ownerName : "Client",
                ownerEmail: targetPet ? targetPet.ownerEmail : "",
                ownerPhone: targetPet ? targetPet.ownerPhone : "",
                vaccineTypeId: targetVaccine ? targetVaccine.id : recPayload.vaccineTypeId,
                vaccineName: targetVaccine ? targetVaccine.name : "Vaccine Protocol",
                standardIntervalDays: interval,
                dateAdministered: recPayload.dateAdministered,
                nextDueDate: dueStr,
                daysRemainingUntilDue: diffDays,
                status: st,
                administeredBy: recPayload.administeredBy || "Veterinarian",
                batchNumber: recPayload.batchNumber || "LOT-01",
                notes: recPayload.notes || ""
            };
            FALLBACK_STORE.vaccinations.unshift(newRecord);
            if (targetPet) targetPet.vaccinationRecordCount = (targetPet.vaccinationRecordCount || 0) + 1;
            return newRecord;
        }
        return FALLBACK_STORE.vaccinations.slice();
    }

    if (cleanUrl === '/api/vaccinations/due-soon') {
        var daysParam = 7;
        var qParts = endpoint.split('?');
        if (qParts.length > 1) {
            var qParams = new URLSearchParams(qParts[1]);
            if (qParams.get('days')) daysParam = parseInt(qParams.get('days'), 10);
        }
        return FALLBACK_STORE.vaccinations.filter(function(v) { return v.daysRemainingUntilDue >= 0 && v.daysRemainingUntilDue <= daysParam; });
    }

    if (cleanUrl === '/api/vaccinations/overdue') {
        return FALLBACK_STORE.vaccinations.filter(function(v) { return v.daysRemainingUntilDue < 0; });
    }

    var vacMatch = endpoint.match(/\/api\/vaccinations\/(\d+)/);
    if (vacMatch && method === 'DELETE') {
        var vDelId = parseInt(vacMatch[1], 10);
        FALLBACK_STORE.vaccinations = FALLBACK_STORE.vaccinations.filter(function(v) { return v.id !== vDelId; });
        return null;
    }

    return [];
}

// Enterprise Toast Notifications
function showToast(message, type) {
    type = type || 'success';
    var shelf = document.getElementById('toastShelf');
    if (!shelf) {
        shelf = document.createElement('div');
        shelf.id = 'toastShelf';
        shelf.className = 'toast-shelf';
        document.body.appendChild(shelf);
    }

    var toast = document.createElement('div');
    toast.className = 'toast-item ' + type;

    var iconSvg = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#16a34a" stroke-width="2.5"><polyline points="20 6 9 17 4 12"></polyline></svg>';
    if (type === 'error') iconSvg = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#dc2626" stroke-width="2.5"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>';
    if (type === 'warning') iconSvg = '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#d97706" stroke-width="2.5"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"></path></svg>';

    toast.innerHTML = '<div style="flex-shrink: 0;">' + iconSvg + '</div>' +
                      '<div style="flex: 1; font-weight: 500;">' + escapeHtml(message) + '</div>';

    shelf.appendChild(toast);

    setTimeout(function() {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(-8px)';
        toast.style.transition = 'all 0.2s ease';
        setTimeout(function() { toast.remove(); }, 250);
    }, 4000);
}

function escapeHtml(text) {
    if (!text) return '';
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function formatDate(dateStr) {
    if (!dateStr) return '—';
    var parts = dateStr.split('-');
    if (parts.length === 3) {
        var d = new Date(parts[0], parts[1] - 1, parts[2]);
        return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    }
    return dateStr;
}

function getDueStatusBadge(status, daysRemaining) {
    if (status === 'OVERDUE' || daysRemaining < 0) {
        var overdueDays = Math.abs(daysRemaining);
        return '<span class="status-pill status-overdue"><span class="status-dot"></span>Overdue (' + overdueDays + 'd)</span>';
    }
    if (status === 'DUE_SOON' || daysRemaining <= 7) {
        return '<span class="status-pill status-due"><span class="status-dot"></span>Due in ' + daysRemaining + 'd</span>';
    }
    return '<span class="status-pill status-ok"><span class="status-dot"></span>Current (' + daysRemaining + 'd)</span>';
}

function openModal(modalId) {
    var modal = document.getElementById(modalId);
    if (modal) modal.classList.add('is-open');
}

function closeModal(modalId) {
    var modal = document.getElementById(modalId);
    if (modal) modal.classList.remove('is-open');
}

// Complete Navigation Generator & Dynamic Highlighter
function getHeaderHtml(activePage) {
    var navItems = [
        { id: 'dashboard', href: 'index.html', label: 'Dashboard' },
        { id: 'pets', href: 'pets.html', label: 'Patients' },
        { id: 'owners', href: 'owners.html', label: 'Clients' },
        { id: 'vaccinate', href: 'vaccinate.html', label: 'Administer' },
        { id: 'due-soon', href: 'due-soon.html', label: 'Schedule', hasBadge: true },
        { id: 'history', href: 'history.html', label: 'Records' },
        { id: 'vaccines', href: 'vaccines.html', label: 'Protocols' }
    ];

    var menuHtml = navItems.map(function(item) {
        var isActive = (item.id === activePage) ? ' active' : '';
        var badgeHtml = item.hasBadge ? ' <span id="nav-due-badge" class="count-pill" style="display:none;">0</span>' : '';
        return '<li><a href="' + item.href + '" class="nav-item-link' + isActive + '" id="nav-' + item.id + '">' + item.label + badgeHtml + '</a></li>';
    }).join('');

    return (
        '<header class="app-header">' +
            '<div class="header-container">' +
                '<a href="index.html" class="brand-section">' +
                    '<div class="brand-badge">' +
                        '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">' +
                            '<path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>' +
                        '</svg>' +
                    '</div>' +
                    '<div>' +
                        '<span class="brand-name">PetCareBook</span>' +
                        '<span class="brand-sub">Clinical CIS</span>' +
                    '</div>' +
                '</a>' +
                '<nav>' +
                    '<ul class="nav-menu" id="mainNavMenu">' +
                        menuHtml +
                    '</ul>' +
                '</nav>' +
                '<div class="nav-actions">' +
                    '<a href="vaccinate.html" class="btn btn-primary btn-sm">+ Record Vaccine</a>' +
                '</div>' +
            '</div>' +
        '</header>'
    );
}

function renderNavbar(activePage) {
    // Inject header markup into container if needed
    var container = document.getElementById('navbar-container');
    if (container) {
        container.innerHTML = getHeaderHtml(activePage);
    }

    // Ensure matching link has .active class even if static HTML was used
    var links = document.querySelectorAll('.nav-item-link');
    if (links.length > 0) {
        links.forEach(function(l) {
            var href = l.getAttribute('href');
            if ((activePage === 'dashboard' && href === 'index.html') ||
                (activePage === 'pets' && href === 'pets.html') ||
                (activePage === 'owners' && href === 'owners.html') ||
                (activePage === 'vaccinate' && href === 'vaccinate.html') ||
                (activePage === 'due-soon' && href === 'due-soon.html') ||
                (activePage === 'history' && href === 'history.html') ||
                (activePage === 'vaccines' && href === 'vaccines.html')) {
                l.classList.add('active');
            } else {
                l.classList.remove('active');
            }
        });
    }

    // Update 7-Day & Overdue Due Badge dynamically
    apiFetch('/api/dashboard/stats')
        .then(function(stats) {
            var badge = document.getElementById('nav-due-badge');
            if (badge && stats) {
                var totalUrgent = (stats.dueInNext7DaysCount || 0) + (stats.overdueCount || 0);
                if (totalUrgent > 0) {
                    badge.textContent = totalUrgent;
                    badge.style.display = 'inline-block';
                } else {
                    badge.style.display = 'none';
                }
            }
        })
        .catch(function() {});
}

function renderFooter() {
    var footer = document.getElementById('footer-container');
    if (!footer) return;
    footer.innerHTML = 
        '<footer class="app-footer">' +
            '<div class="footer-inner">' +
                '<div><strong>PetCareBook Clinical Information System</strong> &bull; Protocol-Based Immunization Schedule</div>' +
                '<div>Runtime: Spring Boot &bull; JDBC Data Layer (H2 / MySQL)</div>' +
            '</div>' +
        '</footer>';
}
