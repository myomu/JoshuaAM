"use strict";(self.webpackChunkjoshuaam=self.webpackChunkjoshuaam||[]).push([[927],{9927:(e,t,a)=>{a.r(t),a.d(t,{default:()=>g});var s=a(5043),n=a(3216),o=a(7154),l=a(3814),r=a(1072),c=a(8602),i=a(4282),d=a(579);const h=function(){const e=(0,n.Zp)(),[t,a]=(0,s.useState)(""),[h,p]=(0,s.useState)(!1);return(0,d.jsxs)("div",{children:[(0,d.jsx)("div",{children:"\uadf8\ub8f9 \ucd94\uac00 \ud654\uba74\uc785\ub2c8\ub2e4~"}),(0,d.jsxs)(l.A,{noValidate:!0,validated:h,onSubmit:a=>{!1===a.currentTarget.checkValidity()&&(a.preventDefault(),a.stopPropagation()),p(!0),a.preventDefault();const s={groupName:t};o.A.post("http://localhost:8080/api/groups/create",s,{headers:{"Content-Type":"application/json"}}).then((t=>{console.log(t.data),alert("\uadf8\ub8f9 \ucd94\uac00\uc5d0 \uc131\uacf5\ud558\uc600\uc2b5\ub2c8\ub2e4."),e("/groups")})).catch((function(e){alert("\uadf8\ub8f9 \ucd94\uac00\uc5d0 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.")}))},children:[(0,d.jsx)(r.A,{className:"mb-3",children:(0,d.jsxs)(l.A.Group,{as:c.A,md:"6",controlId:"groupName",children:[(0,d.jsx)(l.A.Label,{children:"\uadf8\ub8f9 \uc774\ub984"}),(0,d.jsx)(l.A.Control,{required:!0,type:"text",placeholder:"\uc774\ub984",value:t,onChange:e=>{a(e.target.value)}}),(0,d.jsx)(l.A.Control.Feedback,{type:"invalid",children:"\uc774\ub984\uc744 \uc785\ub825\ud574\uc8fc\uc138\uc694."})]})}),(0,d.jsx)(i.A,{type:"submit",className:"btnSave",children:"\uc800\uc7a5"}),(0,d.jsx)(i.A,{onClick:()=>{e("/groups")},className:"btnCancel",children:"\ucde8\uc18c"})]})]})};var p=a(4196);const u=function(){const{groupId:e}=(0,n.g)(),t=(0,n.Zp)(),[a,h]=(0,s.useState)(""),[p,u]=(0,s.useState)(!1);return(0,s.useEffect)((()=>{o.A.get("http://localhost:8080/api/groups/edit/"+e).then((e=>{h(e.data.groupName),console.log(e.data)}))}),[e]),(0,d.jsxs)("div",{children:[(0,d.jsx)("p",{children:"\uadf8\ub8f9 \uc218\uc815"}),(0,d.jsxs)(l.A,{noValidate:!0,validated:p,onSubmit:t=>{!1===t.currentTarget.checkValidity()&&(t.preventDefault(),t.stopPropagation()),u(!0),t.preventDefault();const s={groupName:a};o.A.post("http://localhost:8080/api/groups/edit/"+e,s,{headers:{"Content-Type":"application/json"}}).then((e=>{console.log(e.data),alert("\uadf8\ub8f9 \uc218\uc815\uc5d0 \uc131\uacf5\ud558\uc600\uc2b5\ub2c8\ub2e4."),window.location.replace("/groups")})).catch((function(e){alert("\uadf8\ub8f9 \uc218\uc815\uc5d0 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.")}))},children:[(0,d.jsx)(r.A,{className:"mb-3",children:(0,d.jsxs)(l.A.Group,{as:c.A,md:"6",controlId:"groupName",children:[(0,d.jsx)(l.A.Label,{children:"\uadf8\ub8f9 \uc774\ub984"}),(0,d.jsx)(l.A.Control,{required:!0,type:"text",placeholder:"\uc774\ub984",value:a,onChange:e=>{h(e.target.value)}}),(0,d.jsx)(l.A.Control.Feedback,{type:"invalid",children:"\uc774\ub984\uc744 \uc785\ub825\ud574\uc8fc\uc138\uc694."})]})}),(0,d.jsx)(i.A,{type:"submit",className:"btnSave",children:"\uc800\uc7a5"}),(0,d.jsx)(i.A,{onClick:()=>{t("/groups")},className:"btnCancel",children:"\ucde8\uc18c"})]})]})};var j=a(5107),x=a(4710);const g=function(){let e=(0,n.Zp)();const[t,a]=(0,s.useState)(""),[r,c]=(0,s.useState)([]);return(0,s.useEffect)((()=>{o.A.get("http://localhost:8080/api/groups").then((e=>{a(e.data),console.log("---"),console.log(e)})).catch((e=>console.log(e)))}),[]),(0,d.jsx)("div",{children:(0,d.jsx)(s.Suspense,{fallback:(0,d.jsx)("div",{children:"\ub85c\ub529\uc911\uc784"}),children:(0,d.jsxs)(n.BV,{children:[(0,d.jsx)(n.qh,{path:"/",element:(0,d.jsxs)(d.Fragment,{children:[(0,d.jsx)(i.A,{onClick:()=>{e("create")},children:"\uadf8\ub8f9 \ucd94\uac00"}),(0,d.jsx)("p",{children:"\uadf8\ub8f9 \ub9ac\uc2a4\ud2b8"}),(0,d.jsxs)(l.A,{onSubmit:e=>{e.preventDefault(),console.log(r),r.sort();const t={groupIds:r};o.A.post("http://localhost:8080/api/groups/delete",t,{headers:{"Content-Type":"application/json"}}).then((e=>{console.log(e),alert("\uadf8\ub8f9 \uc0ad\uc81c\uc5d0 \uc131\uacf5\ud558\uc600\uc2b5\ub2c8\ub2e4."),window.location.replace("/groups")})).catch((function(e){console.log(e),alert("\uadf8\ub8f9 \uc0ad\uc81c\uc5d0 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.")}))},children:[(0,d.jsx)(i.A,{variant:"warning",type:"submit",className:"btnSave",disabled:0===r.length,children:"\uadf8\ub8f9 \uc0ad\uc81c"}),(0,d.jsx)(j.$,{label:"\ucd9c\uc11d\uccb4\ud06c \uc778\uc6d0",values:r,onChange:c,children:(0,d.jsxs)(p.A,{bordered:!0,hover:!0,className:"text-nowrap text-center align-middle customTable",children:[(0,d.jsx)("thead",{children:(0,d.jsxs)("tr",{children:[(0,d.jsx)("th",{children:"#"}),(0,d.jsx)("th",{children:"ID"}),(0,d.jsx)("th",{children:"\uc870 \uc774\ub984"}),(0,d.jsx)("th",{children:"\uc218\uc815"})]})}),(0,d.jsx)("tbody",{children:t?t.map(((t,a)=>(0,d.jsxs)("tr",{children:[(0,d.jsx)("td",{children:(0,d.jsx)(x.S,{value:t.groupId})}),(0,d.jsx)("td",{children:a+1}),(0,d.jsx)("td",{children:t.groupName}),(0,d.jsx)("td",{children:(0,d.jsx)(i.A,{variant:"light",onClick:()=>{e("edit/"+t.groupId)},children:(0,d.jsx)("i",{className:"fa-regular fa-pen-to-square"})})})]},a))):null})]})})]})]})}),(0,d.jsx)(n.qh,{path:"/create",element:(0,d.jsx)(h,{})}),(0,d.jsx)(n.qh,{path:"/edit/:groupId",element:(0,d.jsx)(u,{})})]})})})}},4196:(e,t,a)=>{a.d(t,{A:()=>c});var s=a(8139),n=a.n(s),o=a(5043),l=a(7852),r=a(579);const c=o.forwardRef(((e,t)=>{let{bsPrefix:a,className:s,striped:o,bordered:c,borderless:i,hover:d,size:h,variant:p,responsive:u,...j}=e;const x=(0,l.oU)(a,"table"),g=n()(s,x,p&&"".concat(x,"-").concat(p),h&&"".concat(x,"-").concat(h),o&&"".concat(x,"-").concat("string"===typeof o?"striped-".concat(o):"striped"),c&&"".concat(x,"-bordered"),i&&"".concat(x,"-borderless"),d&&"".concat(x,"-hover")),m=(0,r.jsx)("table",{...j,className:g,ref:t});if(u){let e="".concat(x,"-responsive");return"string"===typeof u&&(e="".concat(e,"-").concat(u)),(0,r.jsx)("div",{className:e,children:m})}return m}))}}]);
//# sourceMappingURL=927.117a5f42.chunk.js.map